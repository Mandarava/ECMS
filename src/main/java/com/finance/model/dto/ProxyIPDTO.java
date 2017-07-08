package com.finance.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2017/2/9.
 */
@Setter
@Getter
@ToString
public class ProxyIPDTO implements Serializable {

    private String ip;

    private String port;

    private String location;

    private Double responseTime;

    private String type;

    private String anonymnousLevel;

}
