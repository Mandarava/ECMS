package com.finance.model.dto;

import java.io.Serializable;

/**
 * Created by zt on 2017/2/9.
 */
public class ProxyIPDTO implements Serializable {

    private String ip;

    private String port;

    private String location;

    private Double responseTime;

    private String type;

    private String anonymnousLevel;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnonymnousLevel() {
        return anonymnousLevel;
    }

    public void setAnonymnousLevel(String anonymnousLevel) {
        this.anonymnousLevel = anonymnousLevel;
    }
}
