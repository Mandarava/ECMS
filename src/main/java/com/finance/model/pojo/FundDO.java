package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class FundDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 基金代码
     */
    private String code;

    /**
     * 昨日收益
     */
    private String name;

    /**
     * 基金公司名称
     */
    private String companyName;

    /**
     * 板块名
     */
    private String subjectName;

    /**
     * 基金规模
     */
    private Double fundScale;

    /**
     * 建立时间
     */
    private Date establishDate;

    /**
     * 日涨幅
     */
    private String type;

    private String type1;

    private String updaterId;

    private Date updatetime;

    /**
     * 晨星评级
     */
    private int cxpj;

    /**
     * 银河评级
     */
    private int yhpj;

    /**
     * 海通评级
     */
    private int htpj;

    /**
     * 济安金信
     */
    private int jajxpj;

    /**
     * 招商评级
     */
    private int zspj;

}
