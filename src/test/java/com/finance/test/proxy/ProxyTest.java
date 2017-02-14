package com.finance.test.proxy;

import org.junit.Test;

/**
 * Created by zt on 2017/2/14.
 */
public class ProxyTest {

    @Test
    public void testProxy() {
        AccountService accountService = (AccountService) TransactionProxy.newInstance(new AccountServiceImpl());
        accountService.transfer("A001", "A002", 200);
        accountService.query("A001");
    }

}
