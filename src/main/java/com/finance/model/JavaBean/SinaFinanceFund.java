package com.finance.model.JavaBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2016/10/4.
 */
public class SinaFinanceFund implements Serializable {

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1Id() {
        return type1Id;
    }

    public void setType1Id(String type1Id) {
        this.type1Id = type1Id;
    }

    public String getType2Id() {
        return type2Id;
    }

    public void setType2Id(String type2Id) {
        this.type2Id = type2Id;
    }

    public String getType3Id() {
        return type3Id;
    }

    public void setType3Id(String type3Id) {
        this.type3Id = type3Id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Date getClrq() {
        return clrq;
    }

    public void setClrq(Date clrq) {
        this.clrq = clrq;
    }

    public Double getDwjz() {
        return dwjz;
    }

    public void setDwjz(Double dwjz) {
        this.dwjz = dwjz;
    }

    public Double getLjjz() {
        return ljjz;
    }

    public void setLjjz(Double ljjz) {
        this.ljjz = ljjz;
    }

    public Double getJjgm() {
        return jjgm;
    }

    public void setJjgm(Double jjgm) {
        this.jjgm = jjgm;
    }

    public Double getW1navg() {
        return w1navg;
    }

    public void setW1navg(Double w1navg) {
        this.w1navg = w1navg;
    }

    public Double getW4navg() {
        return w4navg;
    }

    public void setW4navg(Double w4navg) {
        this.w4navg = w4navg;
    }

    public Double getW13navg() {
        return w13navg;
    }

    public void setW13navg(Double w13navg) {
        this.w13navg = w13navg;
    }

    public Double getW26navg() {
        return w26navg;
    }

    public void setW26navg(Double w26navg) {
        this.w26navg = w26navg;
    }

    public Double getW52navg() {
        return w52navg;
    }

    public void setW52navg(Double w52navg) {
        this.w52navg = w52navg;
    }

    public Double getY3navg() {
        return y3navg;
    }

    public void setY3navg(Double y3navg) {
        this.y3navg = y3navg;
    }

    public Double getY5navg() {
        return y5navg;
    }

    public void setY5navg(Double y5navg) {
        this.y5navg = y5navg;
    }

    public Double getSlnavg() {
        return slnavg;
    }

    public void setSlnavg(Double slnavg) {
        this.slnavg = slnavg;
    }

    public Double getYnyl() {
        return ynyl;
    }

    public void setYnyl(Double ynyl) {
        this.ynyl = ynyl;
    }

    public int getCxpj() {
        return cxpj;
    }

    public void setCxpj(int cxpj) {
        this.cxpj = cxpj;
    }

    public int getHtpj() {
        return htpj;
    }

    public void setHtpj(int htpj) {
        this.htpj = htpj;
    }

    public int getJajxpj() {
        return jajxpj;
    }

    public void setJajxpj(int jajxpj) {
        this.jajxpj = jajxpj;
    }

    public int getZspj() {
        return zspj;
    }

    public void setZspj(int zspj) {
        this.zspj = zspj;
    }

    public int getDailypv() {
        return dailypv;
    }

    public void setDailypv(int dailypv) {
        this.dailypv = dailypv;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getYhpj3() {
        return yhpj3;
    }

    public void setYhpj3(int yhpj3) {
        this.yhpj3 = yhpj3;
    }

    public int getYhpj5() {
        return yhpj5;
    }

    public void setYhpj5(int yhpj5) {
        this.yhpj5 = yhpj5;
    }

    public String getBartype() {
        return bartype;
    }

    public void setBartype(String bartype) {
        this.bartype = bartype;
    }

    @Override
    public String toString() {
        return "SinaFinanceFund{" +
                "symbol=" + symbol +
                ", name='" + name + '\'' +
                ", type1Id='" + type1Id + '\'' +
                ", type2Id='" + type2Id + '\'' +
                ", type3Id='" + type3Id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", clrq='" + clrq + '\'' +
                ", dwjz=" + dwjz +
                ", ljjz=" + ljjz +
                ", jjgm=" + jjgm +
                ", w1navg=" + w1navg +
                ", w4navg=" + w4navg +
                ", w13navg=" + w13navg +
                ", w26navg=" + w26navg +
                ", w52navg=" + w52navg +
                ", y3navg=" + y3navg +
                ", y5navg=" + y5navg +
                ", slnavg=" + slnavg +
                ", ynyl=" + ynyl +
                ", cxpj=" + cxpj +
                ", htpj=" + htpj +
                ", jajxpj=" + jajxpj +
                ", zspj=" + zspj +
                ", dailypv=" + dailypv +
                ", trend=" + trend +
                ", exchange='" + exchange + '\'' +
                ", yhpj3=" + yhpj3 +
                ", yhpj5=" + yhpj5 +
                ", bartype='" + bartype + '\'' +
                '}';
    }
}
