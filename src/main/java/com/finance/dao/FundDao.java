package com.finance.dao;

import com.finance.model.pojo.Fund;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/10/2.
 */
@Repository
public interface FundDao {

    /**
     * 查找所有基金
     *
     * @return
     */
    List<Fund> findFunds();

    int insertOrUpdateFundData(Fund fund);

    int findFundByCode(String code);

    int updateFund(Fund fund);

    int insertFund(Fund fund);
}
