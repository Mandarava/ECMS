package com.finance.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/10/4.
 */
@ToString
@Setter
@Getter
public class SinaFinanceFundNetDTO implements Serializable {

    /**
     * 净值日期
     */
    @Expose
    @SerializedName("fbrq")
    private Date fbrq;

    /**
     * 单位净值
     */
    @Expose
    @SerializedName("jjjz")
    private Double jjjz;

    /**
     * 累计净值
     */
    @Expose
    @SerializedName("ljjz")
    private Double ljjz;

    /**
     * 七日年化收益率
     */
    @Expose
    @SerializedName("nhsyl")
    private Double nhsyl;

    /**
     * 万份收益
     */
    @Expose
    @SerializedName("dwsy")
    private Double dwsy;

}
