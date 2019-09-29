package com.notime.mall.passport.contorller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.notime.mall.api.bean.UmsMember;
import com.notime.mall.api.service.UmsMeMberService;
import com.notime.mall.passport.util.JwtUtil;
import com.notime.mall.util.HttpclientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {


    @RequestMapping("index")
    public  String  index(String requestUrl, Model model){
        model.addAttribute("requestUrl",requestUrl);
        return "index";
    }

    @Reference
    UmsMeMberService umsMeMberService;

    @RequestMapping("login")
    @ResponseBody
    public String  login(HttpServletRequest request, UmsMember umsMember){

        // $.post("login",{username:$("#username").val(),password:$("#password").val()},function (token) {
        //调用服务 验证umsMber 是否存在
     UmsMember umsMemberFormDb=   umsMeMberService.loginByUms(umsMember);
        System.err.println(request);
     if (umsMemberFormDb!=null){
         //生成token
         Map<String, Object> map = new HashMap<>();

         System.err.println("getId=="+umsMemberFormDb.getId());
         map.put("memberId",umsMemberFormDb.getId());
         map.put("nickname",umsMemberFormDb.getNickname());
        String salt = request.getRemoteAddr();
        // System.err.println("currentIp"+currentIp );
         String key = JwtUtil.encode("key", map, salt);
         return  key;

     }else {
         return "fail";
     }
    }

    // 微博登录 把账号保存在数据库里
    @RequestMapping("vlogin")
    public  String vlogin(String code,HttpServletRequest request){
        String addr3 ="https://api.weibo.com/oauth2/access_token";
        //?client_id=300196365&client_secret=d06456555c7246c3164151cd4b082d09&grant_type=authorization_code&redirect_uri=http://yeko.mall.com:8086/vlogin&code=802b10c3bb8952a99fd1d7b48b44f8f1";
//
        Map<String,String> map = new HashMap<>();

        map.put("client_id","300196365");
        map.put("client_secret","d06456555c7246c3164151cd4b082d09");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://yeko.mall.com:8086/vlogin");
        map.put("code",code);
        String accessJson = HttpclientUtil.doPost(addr3, map);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(1);
            /*{
    "access_token": "2.00zWvsRC0rma_11b22e8f33204Psw_",
    "remind_in": "157679999",
    "expires_in": 157679999,
    "uid": "2096554241",
    "isRealName": "true"


}
        * */
        String access_token = null;
        String uid = null;
        Map<String,String> jsonObject = JSON.parseObject(accessJson,Map.class);
        if (jsonObject!=null&&jsonObject.size()>0){
             access_token = jsonObject.get("access_token");
             uid = jsonObject.get("uid");
        }

        // access_ token   有生命周期  不要写死

        Map<String,String> userMap = new HashMap<>();
        userMap.put("access_token",access_token);
        userMap.put("uid",uid);

        String addr4="https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;

        String userJson = HttpclientUtil.doGet(addr4,new HashMap<>());


        Map<String,Object> map1 = JSON.parseObject(userJson, Map.class);


        /*{"id":2096554241,"idstr":"2096554241","class":1,"screen_name":"奥氮平_18688","name":"奥氮平_18688","province":"44","city":"1","location":"广东 广州","description":"","url":"","profile_image_url":"https://tvax1.sinaimg.cn/default/images/default_avatar_male_50.gif?KID=imgbed,tva&Expires=1568232923&ssig=%2FfNwQ8ZBMZ","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.640.640.640/549d0121tw1egm1kjly3jj20hs0hsq4f.jpg","profile_url":"u/2096554241","domain":"","weihao":"","gender":"m","followers_count":0,"friends_count":6,"pagefriends_count":0,"statuses_count":0,"video_status_count":0,"favourites_count":0,"created_at":"Wed Apr 20 10:39:08 +0800 2011","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","insecurity":{"sexual_content":false},"ptype":0,"allow_all_comment":true,"avatar_large":"https://tvax1.sinaimg.cn/default/images/default_avatar_male_180.gif?KID=imgbed,tva&Expires=1568232923&ssig=ZGumfUENpy","avatar_hd":"https://tvax1.sinaimg.cn/default/images/default_avatar_male_180.gif?KID=imgbed,tva&Expires=1568232923&ssig=ZGumfUENpy","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"like":false,"like_me":false,"online_status":0,"bi_followers_count":0,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":33554432,"urank":4,"story_read_state":-1,"vclub_member":0,"is_teenager":0,"is_guardian":0,"is_teenager_list":0}
        *
        *
        * */
        // 保存用户信息
        UmsMember umsMember = new UmsMember();
      if (map1!=null&&map1.size()>0){
          umsMember.setSourceUid((String) map1.get(("idstr")));
          umsMember.setNickname((String) map1.get(("screen_name")));
          umsMember.setUsername((String) map1.get(("name")));
          umsMember.setCity((String) map1.get(("city")));
          umsMember.setAccessCode((String) map1.get(("idstr")));
          umsMember.setSourceUid((String) map1.get(("idstr")));
      }
        umsMember.setSourceType("2");
        umsMember.setAccessToken(access_token);
        umsMember.setCreateTime(new Date());

        umsMeMberService.addUser(umsMember);

        // 带着token 访问首页
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("memberId",umsMember.getId());
        tokenMap.put("nickname",umsMember.getNickname());
        String salt = request.getRemoteAddr();
        String encode = JwtUtil.encode("key", tokenMap, salt);

        return "redirect:http://yeko.mall.com:8083/index?urlToken="+encode;


    }

}
