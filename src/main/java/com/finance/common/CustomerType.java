package com.finance.common;

public enum CustomerType {

    // 主库
    MASTER("master"),

    // 从库
    SLAVE("slave");

    private String name;

    CustomerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
