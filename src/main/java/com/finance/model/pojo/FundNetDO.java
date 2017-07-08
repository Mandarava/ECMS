package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/10/1.
 */
@ToString
@Setter
@Getter
public class FundNetDO implements Serializable {

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

    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FundNetDO fundNet = (FundNetDO) o;

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
