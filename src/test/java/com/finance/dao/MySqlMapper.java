package com.finance.dao;

import com.finance.model.pojo.FundDO;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zt on 2016/10/5.
 */
public interface MySqlMapper {

    @Select("select * from Fund")
    List<FundDO> getList();
}
