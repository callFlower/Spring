package com.canrui.myspring;

public class MyspringTest {
    public static void main(String[] args) {
        //扫描实体类
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.canrui.myspring.entity");
        System.out.println(applicationContext.getBean("animation"));
        //System.out.println(applicationContext.getBean("favorite"));
    }
}
