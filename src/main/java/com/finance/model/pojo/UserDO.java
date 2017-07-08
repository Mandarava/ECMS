package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/10/3.
 */
@ToString
@Setter
@Getter
public class UserDO implements Serializable {

    private String userId;

    private String userName;

    private String password;

    private String mailAddress;

    private String deleteflg;

    private String updaterId;

    private Date updatetime;

}
