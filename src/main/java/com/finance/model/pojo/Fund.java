package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class Fund implements Serializable {

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
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

    public Double getFundScale() {
        return fundScale;
    }

    public void setFundScale(Double fundScale) {
        this.fundScale = fundScale;
    }

    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public int getCxpj() {
        return cxpj;
    }

    public void setCxpj(int cxpj) {
        this.cxpj = cxpj;
    }

    public int getYhpj() {
        return yhpj;
    }

    public void setYhpj(int yhpj) {
        this.yhpj = yhpj;
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

    @Override
    public String toString() {
        return "Fund{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", fundScale=" + fundScale +
                ", establishDate=" + establishDate +
                ", type='" + type + '\'' +
                ", type1='" + type1 + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", updatetime=" + updatetime +
                ", cxpj=" + cxpj +
                ", yhpj=" + yhpj +
                ", htpj=" + htpj +
                ", jajxpj=" + jajxpj +
                ", zspj=" + zspj +
                '}';
    }
}
