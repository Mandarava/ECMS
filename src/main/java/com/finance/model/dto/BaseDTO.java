package com.finance.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/10/3.
 */
@Getter
@Setter
@ToString
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页信息
     */
    private PageDTO pageDTO = new PageDTO();

}
