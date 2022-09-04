package com.canrui.myspring;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationConfigApplicationContext {

    private Map<String,Object> ioc = new HashMap<>();

    // 自定义一个 MyAnnotationConfigApplicationContext，构造器中传入要扫描的包。
    public AnnotationConfigApplicationContext(String pack) {
        // 第一步、获取这个包下的所有类。(原材料)
        Set<BeanDefinition> beanDefinitions = findBeanDefinition(pack);

        // 第二步、根据原材料创建bean
        createObject(beanDefinitions);

        // 第三步、自动装载
        autowireObject(beanDefinitions);
    }

    //自动装载实现--和根据原材料创造bean差不多
    public void autowireObject(Set<BeanDefinition> beanDefinitions){
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired annotation = declaredField.getAnnotation(Autowired.class);
                if(annotation!=null){
                    Qualifier qualifier = declaredField.getAnnotation(Qualifier.class);
                    if(qualifier!=null){
                        //byName
                        try {
                            String beanName = qualifier.value();
                            Object bean = getBean(beanName);
                            String fieldName = declaredField.getName();
                            String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                            Method method = clazz.getMethod(methodName, declaredField.getType());
                            Object object = getBean(beanDefinition.getBeanName());
                            method.invoke(object, bean);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //bytype
                        try {
                            // 在前面通过declaredFields获取到Animation所有属性，然后到这里的就是有Autowired注解的
                            // 获取setter参数
                            String fieldName = declaredField.getName(); //charater
                            Object bean = getBean(fieldName); // Charater(peopleId=1, presence=2.3)
                            // 获取方法对象
                            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method method = clazz.getMethod(methodName, declaredField.getType());
                            // 获取对象--Animation
                            Object object = getBean(beanDefinition.getBeanName());
                            method.invoke(object, bean);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 这里在提供一个getBean方法
    public Object getBean(String beanName){
        return ioc.get(beanName);
    }

    public void createObject(Set<BeanDefinition> beanDefinitions){
        // 1、遍历得到每个对象
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()){
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            try {
                // 2、创建对象
                Object object = clazz.newInstance();
                // 3、完成属性的赋值
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Value valueAnnotation = declaredField.getAnnotation(Value.class);
                    if (valueAnnotation != null){
                        // 这里拿到value值，都是String
                        String value = valueAnnotation.value();
                        // 通过set方法完成赋值，这里还是通过反射调用实体类的set方法，不过这里需要拼接一下
                        String filename = declaredField.getName();
                        String methodName = "set"+filename.substring(0,1).toUpperCase()+filename.substring(1);
                        // 根据方法名和参数列表获取到Method方法对象
                        Method method = clazz.getMethod(methodName, declaredField.getType());
                        // 完成数据类型转换，比如从String->Integer
                        Object val = null;
                        switch (declaredField.getType().getSimpleName()){
                            case "Integer":
                                val = Integer.parseInt(value);
                                break;
                            case "String":
                                val = value;
                                break;
                            case "Float":
                                val = Float.parseFloat(value);
                                break;
                        }
                        // 通过方法对象来访问方法
                        method.invoke(object,val);
                    }
                }
                // 4、把创建后的对象和beanName放到Map（缓存）里
                ioc.put(beanName,object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<BeanDefinition> findBeanDefinition(String pack){

        // 1、获取包下所有类
        Set<Class<?>> classes = MyTools.getClasses(pack);
        Iterator<Class<?>> iterator = classes.iterator();
        Set<BeanDefinition> beanDefinitions = new HashSet<>();

        // 2、遍历这些类，找到添加了注解的类
        while (iterator.hasNext()){
            // 2.1 获取class对象
            Class<?> clazz = iterator.next();
            // 返回一个注解对象
            Component componentAnnotation = clazz.getAnnotation(Component.class);
            if (componentAnnotation != null){
                // 2.2 获取Component注解的值---beanName
                String beanName = componentAnnotation.value();
                // 如果Component括号里没有写
                if ("".equals(beanName)){
                    // 2.2.1 我们就要类名首字母小写

                    // 我们需要对类名进行处理，因为现在拿到的是包含包名的类名
                    // com.canrui.myspring.entity.Animation
                    // com.canrui.myspring.entity.Charater
                    String className = clazz.getName().replaceAll(clazz.getPackageName() + ".", "");
                    // 拿到类名首字母变小写
                    beanName = className.substring(0,1).toLowerCase()+className.substring(1);
                }
                // 3、将这些类封装成BeanDefinition，装载到集合中
                BeanDefinition beanDefinition = new BeanDefinition(beanName,clazz);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;
    }
}
