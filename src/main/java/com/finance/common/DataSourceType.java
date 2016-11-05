package com.finance.common;

public enum DataSourceType {

    // 主库
    Master("masterDataSource"),

    // 从库
    Slave("slaveDataSource");

    private String name;

    private DataSourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
