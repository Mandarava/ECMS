package com.finance.quartz.service;

import com.finance.quartz.ScheduleJob;

import java.util.List;

/**
 * Created by zt 2017/9/23 14:26
 */
public interface ScheduleJobService {

    List<ScheduleJob> findAllScheduleJobs();

    boolean addJob(ScheduleJob job);

    boolean deleteJob(ScheduleJob job);

    boolean unScheduleJob(ScheduleJob job);

    boolean rescheduleJob(ScheduleJob job);

    boolean triggerJob(ScheduleJob job);

    boolean pauseTrigger(ScheduleJob job);

    boolean resumeTrigger(ScheduleJob job);

}
