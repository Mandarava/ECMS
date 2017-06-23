package com.finance.model.dto;

import org.apache.http.HttpStatus;

/**
 * Created by zt
 * 2017/6/13 23:39
 */
public class HttpClientResponse {

    private int code;

    private String message;

    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isOk() {
        return HttpStatus.SC_OK == code;
    }
}
