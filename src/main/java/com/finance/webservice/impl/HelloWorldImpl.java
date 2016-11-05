package com.finance.webservice.impl;

import com.finance.webservice.HelloWorld;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by zt on 2016/10/3.
 */
@Component("helloWorld")
@WebService
public class HelloWorldImpl implements HelloWorld {

    public String say(String str) {
        return "Hello" + str;
    }
}
