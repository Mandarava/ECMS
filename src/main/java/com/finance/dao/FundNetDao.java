package com.finance.dao;

import com.finance.model.pojo.FundDO;
import com.finance.model.pojo.FundNetDO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/10/2.
 */
@Repository
public interface FundNetDao {

    int batchInsertFundNetData(List<FundNetDO> fundNetList);

    List<FundNetDO> findFundNetDateByCodes(List<FundDO> funds);

    List<FundNetDO> findFundNetByCode(String code);

    List<FundNetDO> findFundNetPage(@Param("limit") int limit, @Param("offset") int offset);

    int findFundNetCount();
}
