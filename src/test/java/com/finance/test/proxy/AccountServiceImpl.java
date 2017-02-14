package com.finance.test.proxy;

/**
 * Created by zt on 2017/2/14.
 */
public class AccountServiceImpl implements AccountService {

    @Override
    public void transfer(String from, String to, int value) {
        System.out.println(String.format(("Transfer %d from %s to %s"), value, from, to));
    }

    @Override
    public void query(String id) {
        System.out.println(String.format(("query account id : %s"), id));
    }
}
