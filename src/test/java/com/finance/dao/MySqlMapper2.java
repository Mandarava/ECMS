package com.finance.dao;

import com.finance.model.pojo.Fund;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zt on 2016/10/5.
 */
public interface MySqlMapper2 {
    @Select("select * from Fund")
    List<Fund> getList();
}
