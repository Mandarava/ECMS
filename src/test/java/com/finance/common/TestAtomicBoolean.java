package com.finance.common;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zt
 * 2017/7/14 21:23
 */
public class TestAtomicBoolean {

    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private static boolean flag = false;

    public static void main(String[] args) {
//        Runnable runnable = new testAtomicBooleanThread();
//        Runnable runnable2 = new testAtomicBooleanThread();
//        new Thread(runnable).start();
//        new Thread(runnable2).start();

        Runnable runnable = new testAtomicBooleanThread2();
        Runnable runnable2 = new testAtomicBooleanThread2();
        new Thread(runnable).start();
        new Thread(runnable2).start();
    }

    static class testAtomicBooleanThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!flag) {
                flag = true;
                System.out.println("开始缓存……");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = false;
            } else {
                System.out.println("不进行缓存");
            }
        }
    }

    static class testAtomicBooleanThread2 implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (atomicBoolean.compareAndSet(false, true)) {
                System.out.println("开始缓存……");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                atomicBoolean.set(false);
            } else {
                System.out.println("不进行缓存");
            }
        }
    }

}
