package com.notime.mall.config;

import com.notime.mall.inteceptors.MyInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebConfig  extends WebMvcConfigurerAdapter {

    // 继承
    // component

    @Autowired
    MyInteceptor myIntercetor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myIntercetor).addPathPatterns("/**");
        super.addInterceptors(registry);

    }
}
