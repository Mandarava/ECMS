package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2016/10/1.
 */
public class FundNet implements Serializable {

    private int id;

    private String code;

    /**
     * 净值日期
     */
    private Date netDate;

    /**
     * 单位净值
     */
    private double unitNetValue;

    /**
     * 累计净值
     */
    private double accumulatedNetValue;

    /**
     * 日增长率
     */
    private double dailyGrowthRate;

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

    public Date getNetDate() {
        return netDate;
    }

    public void setNetDate(Date netDate) {
        this.netDate = netDate;
    }

    public double getUnitNetValue() {
        return unitNetValue;
    }

    public void setUnitNetValue(double unitNetValue) {
        this.unitNetValue = unitNetValue;
    }

    public double getAccumulatedNetValue() {
        return accumulatedNetValue;
    }

    public void setAccumulatedNetValue(double accumulatedNetValue) {
        this.accumulatedNetValue = accumulatedNetValue;
    }

    public double getDailyGrowthRate() {
        return dailyGrowthRate;
    }

    public void setDailyGrowthRate(double dailyGrowthRate) {
        this.dailyGrowthRate = dailyGrowthRate;
    }

    @Override
    public String toString() {
        return "FundNet{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", netDate=" + netDate +
                ", unitNetValue=" + unitNetValue +
                ", accumulatedNetValue=" + accumulatedNetValue +
                ", dailyGrowthRate=" + dailyGrowthRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FundNet fundNet = (FundNet) o;

        if (!code.equals(fundNet.code)) return false;
        return netDate.equals(fundNet.netDate);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + netDate.hashCode();
        return result;
    }
}
