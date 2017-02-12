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
    private PageDTO pageDTO = new PageDTO();


    public PageDTO getPageDTO() {
        return pageDTO;
    }

    public void setPageDTO(PageDTO pageDTO) {
        this.pageDTO = pageDTO;
    }

    @Override
    public String toString() {
        return "BaseDTO{" +
                "pageDTO=" + pageDTO +
                '}';
    }
}
