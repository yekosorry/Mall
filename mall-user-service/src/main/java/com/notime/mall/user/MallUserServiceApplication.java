package com.notime.mall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan(basePackages = "com.notime.mall.user.mapper")
@SpringBootApplication
public class MallUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallUserServiceApplication.class, args);
    }

}
