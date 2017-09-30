package com.finance.model.vo;

import java.io.Serializable;

/**
 * Created by zt 2017/9/30 12:32
 */
public class BaseResponse<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
