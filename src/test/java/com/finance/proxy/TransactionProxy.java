package com.finance.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zt on 2017/2/14.
 */
public class TransactionProxy implements InvocationHandler {

    private Object target;

    private TransactionProxy(Object target) {
        this.target = target;
    }

    public static Object newInstance(Object obj) {
        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new TransactionProxy(obj));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        if (method.getName().equals("transfer")) {
            System.out.println("Begin transaction");
            result = method.invoke(target, args);
            System.out.println("commit transaction");
        } else {
            result = method.invoke(target, args);
        }

        return result;
    }
}
