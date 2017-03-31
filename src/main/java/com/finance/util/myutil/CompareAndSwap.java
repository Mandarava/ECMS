package com.finance.util.myutil;

/**
 * Created by zt on 2017/3/31.
 */
public class CompareAndSwap {

    private int value;

    public static void main(String[] args) {
        final CompareAndSwap cas = new CompareAndSwap();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int expectValue = cas.getValue();
                boolean b = cas.compareAndSet(expectValue, (int) (Math.random() * 101));
                System.out.println(b);
            }
            ).start();
        }
    }

    public synchronized int getValue() {
        return this.value;
    }

    public synchronized int compareAndSwap(int expectValue, int newValue) {
        int oldValue = this.value;
        if (oldValue == expectValue) {
            this.value = newValue;
        }
        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectValue, int newValue) {
        return expectValue == compareAndSwap(expectValue, newValue);
    }
}
