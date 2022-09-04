package com.canrui.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BeanFactory {

    private static Properties properties;
    // 单例的获取bean操作除了第一次生成之外其余的都是从缓存里获取的所以很快
    private static Map<String, Object> cache= new HashMap();

    //静态代码块在类加载时执行，并且只执行一次。
    //静态代码块在一个类中可以编写多个，并且遵循自上而下的顺序依次执行。

    //静态代码块的作用是什么？怎么用？用在哪儿？什么时候用？
    //静态代码块是java为程序员准备一个特殊的时刻这个特殊的时刻被称为类加载时刻。若希望在此刻执行一段特殊的程序，这段程序可以直接放在静态代码块当中。
    //通常在静态代码块当中完成预备工作，先完成数据的准备工具，例如：初始化连接池，解析XML配置文件
    static {
        properties = new Properties();
        try {
            // 使用ClassLoader加载properties配置文件生成对应的输入流
            // 使用properties对象加载输入流
            // 这样我们就把配置文件映射成为了一个对象
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("factory.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getDao(String beanName) {
        // 先判断缓存中是否存在bean
        if(!cache.containsKey(beanName)){
            // 加锁，完成在多线程下的单例
            synchronized (BeanFactory.class){
                if(!cache.containsKey(beanName)){
                    try {
                        // 获取key对应的value值
                        String value = properties.getProperty(beanName);
                        // 获取到全类名：com.canrui.dao.impl.HelloDaoImpl 后根据反射去创建对象
                        Class clazz = Class.forName(value);
                        Object object = clazz.newInstance();
                        cache.put(beanName, object);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return cache.get(beanName);
    }
}