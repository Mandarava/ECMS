package com.finance.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by zt on 2016/10/5.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setSlave() {
        setContextHolder("slaveDataSource");
    }

    public static void setMaster() {
        setContextHolder("masterDataSource");
    }

    public static String getContextHolder() {
        return contextHolder.get();
    }

    public static void setContextHolder(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 获取当前数据源连接
        return getContextHolder();
    }
}