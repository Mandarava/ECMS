package com.finance.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceContextHolder {

    public static final String SLAVE_DATA_SOURCE = "slaveDataSource";
    public static final String MASTER_DATA_SOURCE = "masterDataSource";
    private static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);
    /* 线程本地环境*/
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 获取数据源类型
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 设置数据源类型
     *
     * @param type 数据源类型
     */
    public static void setDataSourceType(String type) {
        logger.debug("==============切换数据源，类型：" + type + "================");
        contextHolder.set(type);
    }

    /**
     * 清除数据源类型
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    public static void setSlave() {
        setDataSourceType(SLAVE_DATA_SOURCE);
    }

    public static void setMaster() {
        setDataSourceType(MASTER_DATA_SOURCE);
    }

}
