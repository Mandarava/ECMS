package com.finance.quartz.service.impl;

import com.finance.dao.ScheduleJobDAO;
import com.finance.quartz.DynamicScheduler;
import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zt 2017/9/23 14:31
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);

    @Resource
    private ScheduleJobDAO scheduleJobDAO;

    @Override
    public List<ScheduleJob> findAllScheduleJobs() {
        return scheduleJobDAO.findAllScheduleJobs();
    }

    @Override
    @Transactional
    public boolean addJob(ScheduleJob job) {
        scheduleJobDAO.insertScheduleJob(job);
        try {
            DynamicScheduler.addJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteJob(ScheduleJob job) {
        scheduleJobDAO.deleteScheduleJob(job);
        try {
            DynamicScheduler.deleteJob(job);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean rescheduleJob(ScheduleJob job) {
        scheduleJobDAO.updateCronExpression(job);
        try {
            DynamicScheduler.rescheduleJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean unScheduleJob(ScheduleJob job) {
        try {
            DynamicScheduler.unScheduleJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean triggerJob(ScheduleJob job) {
        try {
            DynamicScheduler.triggerJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean pauseTrigger(ScheduleJob job) {
        try {
            DynamicScheduler.pauseTrigger(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean resumeTrigger(ScheduleJob job) {
        try {
            DynamicScheduler.resumeTrigger(job);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

}
