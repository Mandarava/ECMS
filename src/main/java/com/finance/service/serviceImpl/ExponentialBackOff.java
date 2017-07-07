package com.finance.service.serviceImpl;

import com.finance.service.ExponentialBackOffFunction;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

/**
 * Created by zt
 * 2017/7/7 22:57
 */
public class ExponentialBackOff {

    private static final int[] FIBONACCI = new int[]{1, 1, 2, 3, 5, 8, 13};
    private static final List<Class<? extends Exception>> EXPECTED_COMMUNICATION_ERRORS = Arrays.asList(
            SSLHandshakeException.class, SocketTimeoutException.class);

    private ExponentialBackOff() {

    }

    public static <T> T execute(ExponentialBackOffFunction<T> function) {
        for (int attempt = 0; attempt < FIBONACCI.length; attempt++) {
            try {
                return function.execute();
            } catch (Exception e) {
                handleFailure(attempt, e);
            }
        }
        throw new RuntimeException("Failed to execute method.");
    }

    private static void handleFailure(int attempt, Exception e) {
        if (e.getCause() != null && !EXPECTED_COMMUNICATION_ERRORS.contains(e.getCause().getClass()))
            throw new RuntimeException(e);
        doWait(attempt);
    }

    private static void doWait(int attempt) {
        try {
            Thread.sleep(FIBONACCI[attempt] * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ExponentialBackOff.execute(() -> test());
    }

    private static boolean test() {
        System.out.println(111);
        throw new RuntimeException("test failure");
    }
}
