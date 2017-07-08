package com.finance.model.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2017/2/7.
 */
@ToString
@Setter
@Getter
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

}
