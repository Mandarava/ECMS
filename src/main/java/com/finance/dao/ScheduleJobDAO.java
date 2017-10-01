package com.finance.dao;

import com.finance.quartz.ScheduleJob;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt 2017/10/1 10:06
 */
@Repository
public interface ScheduleJobDAO {

    List<ScheduleJob> findAllScheduleJobs();

    int insertScheduleJob(ScheduleJob job);

    int deleteScheduleJob(ScheduleJob job);

    int updateCronExpression(ScheduleJob job);

}
