package com.finance.util.myutil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zt on 2017/3/31.
 */
public class AtomicData implements Runnable {

    private AtomicInteger atomicData = new AtomicInteger(0);

    public static void main(String[] args) {
        AtomicData ad = new AtomicData();
        for (int i = 0; i < 10; i++) {
            new Thread(ad).start();
        }
    }

    @Override
    public void run() {
        System.out.println(getAtomicData());
    }

    public int getAtomicData() {
        return atomicData.getAndIncrement();
    }
}
