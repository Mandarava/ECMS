package com.finance.model.vo;

import com.finance.model.pojo.ProfitDO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zt
 * 2017/12/16 17:49
 */
public class FundProfitSumVO implements Serializable {

    private String code;

    private String name;

    private List<ProfitDO> profits;

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

    public List<ProfitDO> getProfits() {
        return profits;
    }

    public void setProfits(List<ProfitDO> profits) {
        this.profits = profits;
    }
}
