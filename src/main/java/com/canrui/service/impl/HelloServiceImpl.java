package com.canrui.service.impl;

import com.canrui.dao.HelloDao;
import com.canrui.dao.impl.HelloDaoImpl;
import com.canrui.factory.BeanFactory;
import com.canrui.service.HelloService;

import java.util.List;

public class HelloServiceImpl implements HelloService {

    private HelloDao helloDao = (HelloDao) BeanFactory.getDao("helloDao");

    public HelloServiceImpl() {
        for (int i = 0; i < 10; i++) {
            System.out.println(BeanFactory.getDao("helloDao"));
        }
    }

    @Override
    public List<String> findAll() {
        return helloDao.findAll();
    }
}
