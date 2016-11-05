package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2016/10/3.
 */
public class Profit implements Serializable {

    private int id;

    private String code;

    private Date time;

    private double profit;

    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Profit{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", time=" + time +
                ", profit=" + profit +
                ", userId='" + userId + '\'' +
                '}';
    }
}
