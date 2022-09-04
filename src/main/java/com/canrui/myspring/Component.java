package com.canrui.myspring;


import lombok.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// component一般都是给类加的，所以括号里写这个
@Target(ElementType.TYPE)
// 运行时机--我们是在运行时使用
@Retention(RetentionPolicy.RUNTIME)
// 定义注解
public @interface Component {
    // 这行用于Component()括号内要传String值,default不写,括号还为空的话就报错
    String value() default "";
}
