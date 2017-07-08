package com.finance.model.dto;

import org.apache.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zt
 * 2017/6/13 23:39
 */
@Setter
@Getter
public class HttpClientResponse {

    private int code;

    private String message;

    private String data;

    public boolean isOk() {
        return HttpStatus.SC_OK == code;
    }
}
