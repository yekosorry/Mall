package com.notime.mall.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;

public class MyCookieUtil {
    // 获取cookie的值 判断购物车是否有数据

    public static String getCookieValue(HttpServletRequest request, String cookieName, Boolean isDecoder) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0 || cookieName == null) {
            return null;
        }


        String cookieValue = null;
        for (Cookie cookie : cookies) {
            //为什么不行啊
       // for (int i = 0; i < cookies.length; i++) {
            if (Objects.equals(cookieName, cookie.getName())) {
                if (isDecoder) {
                    try {
                        cookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    cookieValue = cookie.getValue();
                }
                break;
            }

        }
        return cookieValue;

        //````````````````````````````````````````````````//
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null || cookieName == null){
//            return null;
//        }
//        String retValue = null;
//        try {
//            for (int i = 0; i < cookies.length; i++) {
//                if (cookies[i].getName().equals(cookieName)) {
//                    if (isDecoder) {//如果涉及中文
//                        retValue = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
//                    } else {
//                        retValue = cookies[i].getValue();
//                    }
//                    break;
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return retValue;
    }


    // 设置cookie的值 将购物车信息存在cookie中

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        if (cookieValue == null) {
            cookieValue = "";
        } else {
            try {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 设置到domain中去

        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (cookieMaxage >= 0) {
            cookie.setMaxAge(cookieMaxage);
        }
        if (request != null) {
            //   获取域名一个方法
            cookie.setDomain(getDomainName(request));
        }

        // 在域名路径下保存
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    private static String getDomainName(HttpServletRequest request) {
        String domainName = null;
        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.endsWith("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            // http:// ?
            serverName = serverName.substring(7);
            System.err.println("servername substring=" + serverName);

            final int end = serverName.indexOf("/");

            serverName = serverName.substring(0,end);
            String[] domains = serverName.split("\\.");
            //  转义成字符 \.
            int length = domains.length;
            if (length>3){
                //www.xxx.com.cn
                domainName = domains[length-3]+"."+domains[length-2]+"."+domains[length-1];
                System.err.println("domains[length-3]="+domains[length-3]);
                System.err.println("domains[length-2]="+domains[length-2]);
                System.err.println("domains[length-1]="+domains[length-1]);
            }else if(length<=3&&length>1){
                //xxx.com xxx.cn
                domainName = domains[length-2]+"."+domains[length-1];
            }else{
                domainName = serverName;
            }
        }


        //
        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        System.out.println("domainName = " + domainName);
        return domainName;

    }


    // 删除cookie内容  通过key
        // 赋值为null
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        setCookie(request, response, cookieName, null, 0, false);
    }

}
