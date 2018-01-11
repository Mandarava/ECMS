package com.finance.test.transaction;

import com.finance.dao.FundNetDao;
import com.finance.model.pojo.FundNetDO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zt 2017/11/25 22:03
 */
@Service
public class TransactionTestImpl implements TransactionTest {

    @Autowired
    private TransactionTest transactionTest;

    @Resource
    private FundNetDao fundNetDao;

    public void insertA() {
        fundNetDao.batchInsertFundNetData(mockData()); // 这个也会回滚啊!
        try {
            // !!!! this method will rollback
            // 加try catch,insertB()抛出异常时
            // 调用insertB的外层会受到org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
            // 而把insertB真正的异常吃掉了。
            // A和B全部回滚
            transactionTest.insertB();
//            TransactionTest transactionTest = (TransactionTest) AopContext.currentProxy();
//            transactionTest.insertB();
        } catch (Exception e) {

        }
    }

    public void insertA2() {
        fundNetDao.batchInsertFundNetData(mockData());
        try {
            // !!!  this will not rollback
            // 相当于普通方法invoke, 然后异常被捕获 insertA的事务管理器无法知道异常，A和B方法都正常提交
            insertB();
        } catch (Exception e) {

        }
    }

    public void insertB() {
        fundNetDao.batchInsertFundNetData(mockData());
        throw new RuntimeException("error occurred!");
    }

    private List<FundNetDO> mockData() {
        List<FundNetDO> funds = new ArrayList<>();
        FundNetDO fundNetDO = new FundNetDO();
        fundNetDO.setCode("666666");
        funds.add(fundNetDO);
        return funds;
    }

}
