package com.notime.mall.inteceptors;

import com.notime.mall.annotations.LoginRequired;
import com.notime.mall.util.CookieUtil;
import com.notime.mall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class MyInteceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 对有注解的才实现拦截
            // 获取注解
        //6798
        System.err.println(request);
        LoginRequired methodAnnotation = null;
        if(handler instanceof  HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
             methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
        } else {
            return true;
        }



        if (methodAnnotation == null){
            return true;
        }
//        if (methodAnnotation.needSuccess()==false){
//            // 我需要id 放  // 放可以但先把id 设进request里啊
//            return true;
//        }
        // 获取token 进行判断是否登录
        String token = "";
        String cookieToken = CookieUtil.getCookieValue(request, "yeko", true);
         // window.location.href = ReturnUrl+"?urlToken="+token;
        String  urlToken = request.getParameter("urlToken");


        //获取最新的token
        if(StringUtils.isNotBlank(cookieToken)){
            token = cookieToken;
        }

        if(StringUtils.isNotBlank(urlToken)){
            token = urlToken;
        }


        // 有没有登录
        if (StringUtils.isBlank(token)){
            if (methodAnnotation.needSuccess()==false){
                // 我需要id 放  // 放可以但先把id 设进request里啊
                return true;
            }

            // 带请求页面去登录
            StringBuffer requestURL = request.getRequestURL();
            response.sendRedirect("http://yeko.mall.com:8086/index?requestUrl="+requestURL);
            return false;
        }else {
            // 有token 就校验token
            String salt = request.getRemoteAddr();
            Map<String, Object> key = JwtUtil.decode(token, "key", salt);
            if (key!= null){
                //获取memeberId 和nickname memberId
                System.err.println( key.get("memberId")+"   key.get(\"memberId\")");
                request.setAttribute("memberId", key.get("memberId"));
                request.setAttribute("nickname", key.get("nickname"));

                if (methodAnnotation.needSuccess()==false){
                    // 我需要id 放  // 放可以但先把id 设进request里啊
                    return true;
                }
                CookieUtil.setCookie(request,response,"yeko",token,60*30,true);
                return true;
            }else {
                // 去登录把
                StringBuffer requestURL = request.getRequestURL();

                // request 是不一样的 需要把当前客户的地址传过去
                // 调用Passport的是内部调用是 服务器本机的request
                response.sendRedirect("http://yeko.mall.com:8086/index?requestUrl="+requestURL);
                return false;
            }


        }

    }
}
