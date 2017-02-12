package com.finance.model.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2017/2/7.
 */
public class FundOrderDO implements Serializable {

    private Integer id;

    private String code;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;

    private Double buyAmount;

    private Double buyNetValue;

    private Double share;

    private Double fee;

    private String orderType;

    private String userId;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(Double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public Double getBuyNetValue() {
        return buyNetValue;
    }

    public void setBuyNetValue(Double buyNetValue) {
        this.buyNetValue = buyNetValue;
    }

    public Double getShare() {
        return share;
    }

    public void setShare(Double share) {
        this.share = share;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "FundOrderDO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", buyDate=" + buyDate +
                ", buyAmount=" + buyAmount +
                ", buyNetValue=" + buyNetValue +
                ", share=" + share +
                ", fee=" + fee +
                ", orderType='" + orderType + '\'' +
                ", userId='" + userId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
