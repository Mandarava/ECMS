package com.finance.quartz.service.impl;

import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;

import org.quartz.Scheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zt 2017/9/23 14:31
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Override
    public List<ScheduleJob> findAllScheduleJobs() {
        List<ScheduleJob> scheduleJobList = new ArrayList<>();
        // query data...
        ScheduleJob scheduleJobCluster = new ScheduleJob();
        scheduleJobCluster.setJobId(UUID.randomUUID().toString());
        scheduleJobCluster.setJobName("clusterJobTest");
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

}
