package com.finance.service;

/**
 * Created by zt
 * 2017/7/7 22:56
 */
@FunctionalInterface
public interface ExponentialBackOffFunction<T> {
    T execute();
}
