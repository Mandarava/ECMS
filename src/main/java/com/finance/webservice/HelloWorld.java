package com.finance.webservice;

import javax.jws.WebService;

/**
 * Created by zt on 2016/10/3.
 */
@WebService
public interface HelloWorld {
    String say(String str);
}