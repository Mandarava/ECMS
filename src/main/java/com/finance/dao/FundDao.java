package com.finance.dao;

import com.finance.model.pojo.FundDO;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/10/2.
 */
@Repository
public interface FundDao {

    /**
     * 查找所有的基金
     */
    @Cacheable(cacheName = "baseCache")
    List<FundDO> findFunds();

    @TriggersRemove(cacheName = "baseCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    int batchInsertOrUpdateFundData(List<FundDO> fund);

    @TriggersRemove(cacheName = "baseCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    int insertOrUpdateFundData(FundDO fund);

    @TriggersRemove(cacheName = "baseCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    int updateFund(FundDO fund);

    @TriggersRemove(cacheName = "baseCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    int insertFund(FundDO fund);

    @TriggersRemove(cacheName = "baseCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    int batchInsertFund(List<FundDO> funds);

    @TriggersRemove(cacheName = "baseCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    int batchUpdateFund(List<FundDO> funds);
}
