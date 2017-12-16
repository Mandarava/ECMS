package com.finance.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/10/3.
 */
@ToString
@Setter
@Getter
public class ProfitDO implements Serializable {

    private int id;

    private String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date time;

    private double profit;

    private String userId;

}
