package com.finance.dao;

import com.finance.model.pojo.Fund;
import com.googlecode.ehcache.annotations.Cacheable;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/10/2.
 */
@Repository
public interface FundDao {

    /**
     * 查找所有基金
     */
    @Cacheable(cacheName = "baseCache")
    List<Fund> findFunds();

    int insertOrUpdateFundData(Fund fund);

    int updateFund(Fund fund);

    int insertFund(Fund fund);

    int batchInsertFund(List<Fund> funds);

    int batchUpdateFund(List<Fund> funds);
}
