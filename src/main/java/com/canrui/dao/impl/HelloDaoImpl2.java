package com.canrui.dao.impl;

import com.canrui.dao.HelloDao;

import java.util.Arrays;
import java.util.List;

public class HelloDaoImpl2 implements HelloDao {
    @Override
    public List<String> findAll() {
        return Arrays.asList("can","rui");
    }
}
