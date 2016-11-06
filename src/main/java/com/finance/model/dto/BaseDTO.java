package com.finance.model.dto;

import java.io.Serializable;

/**
 * Created by zt on 2016/10/3.
 */
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 分页信息
     */
    private Page page = new Page();


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
