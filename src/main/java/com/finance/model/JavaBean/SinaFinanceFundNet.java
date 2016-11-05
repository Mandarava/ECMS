package com.finance.model.JavaBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2016/10/4.
 */
public class SinaFinanceFundNet implements Serializable {

    @Expose
    @SerializedName("fbrq")
    private Date fbrq;

    @Expose
    @SerializedName("jjjz")
    private double jjjz;

    @Expose
    @SerializedName("ljjz")
    private double ljjz;

    public Date getFbrq() {
        return fbrq;
    }

    public void setFbrq(Date fbrq) {
        this.fbrq = fbrq;
    }

    public double getJjjz() {
        return jjjz;
    }

    public void setJjjz(double jjjz) {
        this.jjjz = jjjz;
    }

    public double getLjjz() {
        return ljjz;
    }

    public void setLjjz(double ljjz) {
        this.ljjz = ljjz;
    }

    @Override
    public String toString() {
        return "SinaFinanceFundNet{" +
                "fbrq=" + fbrq +
                ", jjjz=" + jjjz +
                ", ljjz=" + ljjz +
                '}';
    }
}
