package com.finance.quartz.quartz22;

import com.finance.quartz.DynamicScheduler;
import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;
import com.finance.util.ApplicationContextUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

public class SchedulerBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = Logger.getLogger(SchedulerBeanPostProcessor.class);
    private static Scheduler schedulerCluster = (Scheduler) ApplicationContextUtil.getBean("quartzCluster");
    private static Scheduler schedulerLocal = (Scheduler) ApplicationContextUtil.getBean("quartzNonCluster");

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        try {
            List<ScheduleJob> scheduleJobs = scheduleJobService.findAllScheduleJobs();
            if (CollectionUtils.isNotEmpty(scheduleJobs)) {
                for (ScheduleJob scheduleJob : scheduleJobs) {
                    if (!scheduleJob.isDeleted()) {
                        // 区分本机运行或集群运行
                        Scheduler scheduler;
                        if (scheduleJob.isCluster()) {
                            scheduler = schedulerCluster;
                        } else {
                            scheduler = schedulerLocal;
                        }
                        JobDetail jobDetail = DynamicScheduler.createJobDetail(scheduleJob);
                        CronTrigger trigger = DynamicScheduler.createTrigger(scheduleJob);
                        DynamicScheduler.addJobToScheduler(jobDetail, scheduler);
                        DynamicScheduler.addTriggerToScheduler(trigger, scheduler);
                    }
                }
            }
            logger.info((scheduleJobs == null ? 0 : scheduleJobs.size()) + "条计划任务已经开始调度!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
