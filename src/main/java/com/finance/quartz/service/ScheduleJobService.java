package com.finance.quartz.service;

import com.finance.quartz.ScheduleJob;

import java.util.List;

/**
 * Created by zt 2017/9/23 14:26
 */
public interface ScheduleJobService {

    List<ScheduleJob> findAllScheduleJobs();

}
