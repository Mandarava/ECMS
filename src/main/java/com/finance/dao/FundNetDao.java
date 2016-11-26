package com.finance.dao;

import com.finance.model.pojo.FundNet;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/10/2.
 */
@Repository
public interface FundNetDao {

    int batchInsertFundNetData(List<FundNet> fundNetList);

    List<FundNet> findFundNetByCode(String code);

    List<FundNet> findFundNetPage(@Param("limit") int limit, @Param("offset") int offset);

    int findFundNetCount();
}
