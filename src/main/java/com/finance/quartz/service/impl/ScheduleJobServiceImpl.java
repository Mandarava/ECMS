package com.finance.quartz.service.impl;

import com.finance.quartz.DynamicScheduler;
import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zt 2017/9/23 14:31
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);

    @Override
    public List<ScheduleJob> findAllScheduleJobs() {
        List<ScheduleJob> scheduleJobList = new ArrayList<>();
        // query data...
        ScheduleJob scheduleJobCluster = new ScheduleJob();
        scheduleJobCluster.setJobId(UUID.randomUUID().toString());
        scheduleJobCluster.setJobName("Cluster_Hello_World");
        scheduleJobCluster.setJobGroup(Scheduler.DEFAULT_GROUP);
        scheduleJobCluster.setCronExpression("0/10 * * * * ?");
        scheduleJobCluster.setTargetObject("helloQuartzService");
        scheduleJobCluster.setTargetMethod("helloWorld");
        scheduleJobCluster.setCluster(true);
        scheduleJobCluster.setConcurrent(false);
        scheduleJobCluster.setRecovery(false);
        scheduleJobCluster.setDurable(true);
        scheduleJobCluster.setDescription("Cluster job test");
        scheduleJobList.add(scheduleJobCluster);
        return scheduleJobList;
    }

    @Override
    public boolean addJob(ScheduleJob job) {
        return false;
    }

    @Override
    public boolean deleteJob(ScheduleJob job) {
        return false;
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
    public boolean rescheduleJob(ScheduleJob job) {
        try {
            DynamicScheduler.rescheduleJob(job);
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
