package com.finance.quartz.service.impl;

import com.finance.quartz.service.HelloQuartzService;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zt 2017/9/21 20:54
 */
@Service("helloQuartzService")
public class HelloQuartzServiceImpl implements HelloQuartzService {

    @Override
    public void helloWorld() {
        System.out.println(new Date() + " Hello World!");
    }

    @Override
    public void print(String value) {
        System.out.println(new Date() + " " + value);
    }

}
