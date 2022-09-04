package com.canrui.dao.impl;

import com.canrui.dao.HelloDao;
import com.canrui.service.HelloService;

import java.util.Arrays;
import java.util.List;

public class HelloDaoImpl implements HelloDao {
    @Override
    public List<String> findAll() {
        return Arrays.asList("1","2","3");
    }
}
