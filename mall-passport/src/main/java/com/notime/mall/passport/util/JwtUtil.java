package com.notime.mall.passport.util;

import io.jsonwebtoken.*;

import java.util.Map;

public class JwtUtil {

    public static void main(String[] args) {
//        Map<String,Object> userMap = new HashMap<>();
//        userMap.put("userId","1");
//        String encode = encode("fuwuqimiyao", userMap, "yanzhi");// 盐值
//
//        System.out.println(encode);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxIn0.VNlLDVfoMremj9_YoGAYDXt2zqCK-KhwnOdqAJu27RQ";

        Map<String, Object> decode = decode(token, "fuwuqimiyao", "yanz123211232131231234qwqwqwrqrqwrqhi");

        System.out.println(decode);
    }

    public static String encode(String key, Map<String,Object> param, String salt){
        if(salt!=null){
            key+=salt;
        }
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key);

        jwtBuilder = jwtBuilder.setClaims(param);

        String token = jwtBuilder.compact();
        return token;

    }


    public  static Map<String,Object>  decode(String token ,String key,String salt){
        Claims claims=null;
        if (salt!=null){
            key+=salt;
        }
        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
           return null;
        }
        return  claims;
    }
}
