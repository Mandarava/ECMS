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
public class SinaFinanceFundDTO implements Serializable {

    @Expose
    @SerializedName("symbol")
    private String symbol;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("Type1Id")
    private String type1Id;

    @Expose
    @SerializedName("Type2Id")
    private String type2Id;

    @Expose
    @SerializedName("Type3Id")
    private String type3Id;

    @Expose
    @SerializedName("CompanyName")
    private String companyName;

    @Expose
    @SerializedName("SubjectName")
    private String subjectName;

    @Expose
    @SerializedName("clrq")
    private Date clrq;

    @SerializedName("dwjz")
    private Double dwjz;

    @SerializedName("ljjz")
    private Double ljjz;

    @Expose
    @SerializedName("jjgm")
    private Double jjgm;

    @SerializedName("w1navg")
    private Double w1navg;

    @SerializedName("w4navg")
    private Double w4navg;

    @SerializedName("w13navg")
    private Double w13navg;

    @SerializedName("w26navg")
    private Double w26navg;

    @SerializedName("w52navg")
    private Double w52navg;

    @SerializedName("y3navg")
    private Double y3navg;

    @SerializedName("y5navg")
    private Double y5navg;

    @SerializedName("slnavg")
    private Double slnavg;

    @SerializedName("ynyl")
    private Double ynyl;

    @Expose
    @SerializedName("cxpj")
    private int cxpj;

    @Expose
    @SerializedName("htpj")
    private int htpj;

    @Expose
    @SerializedName("jajxpj")
    private int jajxpj;

    @Expose
    @SerializedName("zspj")
    private int zspj;

    @SerializedName("dailypv")
    private int dailypv;

    @SerializedName("trend")
    private int trend;

    @SerializedName("EXCHANGE")
    private String exchange;

    @Expose
    @SerializedName("yhpj3")
    private int yhpj3;

    @Expose
    @SerializedName("yhpj5")
    private int yhpj5;

    @SerializedName("bartype")
    private String bartype;

}
