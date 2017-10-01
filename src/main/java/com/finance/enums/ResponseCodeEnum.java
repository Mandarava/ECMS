package com.finance.enums;

/**
 * Created by zt 2017/10/1 21:40
 */
public enum ResponseCodeEnum {

    SUCCESS(200, "success"),
    ERROR(500, "failure");

    private Integer code;

    private String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
