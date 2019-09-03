package com.notime.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.notime.mall.manager.mapper")
@SpringBootApplication
public class MallManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallManagerServiceApplication.class, args);
    }

}

