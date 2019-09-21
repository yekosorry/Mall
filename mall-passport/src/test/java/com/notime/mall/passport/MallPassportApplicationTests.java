package com.notime.mall.passport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallPassportApplicationTests {

    @Test
    public void contextLoads() {
        String key ="300196365";
        String secreat="Secret：d06456555c7246c3164151cd4b082d09";
// 授权页面
        /*
        *
        * https://api.weibo.com/oauth2/authorize?client_id=300196365&response_type=code&redirect_uri=http://yeko.mall.com:8086/vlogin
        * */

        String  addr1 ="https://api.weibo.com/oauth2/authorize?client_id=300196365&response_type=code&redirect_uri=http://yeko.mall.com:8086/vlogin";

  //http://yeko.mall.com:8086/vlogin?code=3e77b8113214e1b2a16adca233608e8b
        String  addr2 ="http://yeko.mall.com:8086/vlogin?code=802b10c3bb8952a99fd1d7b48b44f8f1";

        String addr3 ="https://api.weibo.com/oauth2/access_token?client_id=300196365&client_secret=d06456555c7246c3164151cd4b082d09&grant_type=authorization_code&redirect_uri=http://yeko.mall.com:8086/vlogin&code=802b10c3bb8952a99fd1d7b48b44f8f1";
        /*{
    "access_token": "2.00zWvsRC0rma_11b22e8f33204Psw_",
    "remind_in": "157679999",
    "expires_in": 157679999,
    "uid": "2096554241",
    "isRealName": "true"
}
        * */
        String addr4 = "https://api.weibo.com/2/users/show.json?access_token=2.00zWvsRC0rma_11b22e8f33204Psw_&uid=2096554241";
    }

}
