package com.finance.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zt
 * 2018/4/14 19:32
 */
public class NaiveExceptionHandling {

    public static void main(String[] args) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new ExceptionThread());
            System.out.println(111);
        } catch (RuntimeException e) {
            // This statement will NOT execute!!
            System.out.println("Exception has been handled");
        }
    }

}

class ExceptionThread implements Runnable {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new ExceptionThread());
    }

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
