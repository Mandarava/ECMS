package com.finance.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {

    /* 线程本地环境*/
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    public static List<String> dataSources = new ArrayList<>();

    /**
     * 获取数据源类型
     */
    public static String getCustomerType() {
        return contextHolder.get();
    }

    /**
     * 设置数据源类型
     *
     * @param customerType 数据源类型
     */
    public static void setCustomerType(String customerType) {
        logger.debug("==============切换数据源，类型：" + customerType + "================");
        Assert.notNull(customerType, "customerType cannot be null");
        contextHolder.set(customerType);
    }

    /**
     * 清除数据源类型
     */
    public static void clearCustomerType() {
        contextHolder.remove();
    }

    public static boolean containsDataSource(String dataSourceId) {
        return dataSources.contains(dataSourceId);
    }

}
