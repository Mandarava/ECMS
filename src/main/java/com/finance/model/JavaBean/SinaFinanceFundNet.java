package com.finance.model.JavaBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2016/10/4.
 */
public class SinaFinanceFundNet implements Serializable {

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

    public Date getFbrq() {
        return fbrq;
    }

    public void setFbrq(Date fbrq) {
        this.fbrq = fbrq;
    }

    public Double getJjjz() {
        return jjjz;
    }

    public void setJjjz(Double jjjz) {
        this.jjjz = jjjz;
    }

    public Double getLjjz() {
        return ljjz;
    }

    public void setLjjz(Double ljjz) {
        this.ljjz = ljjz;
    }

    public Double getNhsyl() {
        return nhsyl;
    }

    public void setNhsyl(Double nhsyl) {
        this.nhsyl = nhsyl;
    }

    public Double getDwsy() {
        return dwsy;
    }

    public void setDwsy(Double dwsy) {
        this.dwsy = dwsy;
    }

    @Override
    public String toString() {
        return "SinaFinanceFundNet{" +
                "fbrq=" + fbrq +
                ", jjjz=" + jjjz +
                ", ljjz=" + ljjz +
                ", nhsyl=" + nhsyl +
                ", dwsy=" + dwsy +
                '}';
    }
}
