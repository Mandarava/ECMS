package com.finance.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 捕获异常
 * Created by zt
 * 2018/4/14 19:41
 */
public class CaptureUncaughtException {

    public static void main(String[] args) {
//        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        ExecutorService executorService = Executors.newCachedThreadPool(new HandlerThreadFactory());
        executorService.execute(new ExceptionThread());
    }

}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught " + e);
    }
}

class HandlerThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        System.out.println(this + " creating new Thread");
        Thread thread = new Thread(r);
        System.out.println("created " + thread);
        thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        return thread;
    }
}
