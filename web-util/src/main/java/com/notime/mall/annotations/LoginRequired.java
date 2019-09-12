package com.notime.mall.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // 方法可以用
@Retention(RetentionPolicy.RUNTIME)  // 有效期是整个运行期间
public @interface LoginRequired {

        //默认需要成功
    boolean  needSuccess() default true;

}
