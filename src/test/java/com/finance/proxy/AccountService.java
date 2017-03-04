package com.finance.proxy;

/**
 * Created by zt on 2017/2/14.
 */
public interface AccountService {

    void transfer(String from, String to, int value);

    void query(String id);
}
