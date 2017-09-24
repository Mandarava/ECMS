package com.finance.quartz.quartz22;

import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;
import com.finance.util.ApplicationContextUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.text.ParseException;
import java.util.List;

public class SchedulerBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean overwriteExistingJobs = true;
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
                    // 区分本机运行或集群运行
                    Scheduler scheduler;
                    if (scheduleJob.isCluster()) {
                        scheduler = schedulerCluster;
                    } else {
                        scheduler = schedulerLocal;
                    }
                    JobDetail jobDetail = createJobDetail(scheduleJob);
                    CronTrigger trigger = createTrigger(scheduleJob);
                    addJobToScheduler(jobDetail, scheduler);
                    addTriggerToScheduler(trigger, scheduler);
                }
            }
            logger.info((scheduleJobs == null ? 0 : scheduleJobs.size()) + "条计划任务已经开始调度!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

/*    private void addJobListener(ScheduleJob job, Scheduler scheduler) throws SchedulerException {
        String[] jobListenerNames = job.getJobListenerNames();
        if (jobListenerNames != null) {
            for (int i = 0; i < jobListenerNames.length; i++) {
                scheduler.getListenerManager().addJobListenerMatcher(jobListenerNames[i]
                        , KeyMatcher.keyEquals(new JobKey(job.getJobName(), job.getJobGroup())));
            }
        }
    }*/

    private JobDetail createJobDetail(ScheduleJob job) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduleJob", job);
        return JobBuilder.newJob(job.isConcurrent() ? CustomQuartzJobBean.class : StatefulQuartzJobBean.class)
                .withIdentity(job.getJobName(), job.getJobGroup())
                .withDescription(job.getDescription())
                .requestRecovery(job.isRecovery())
                .storeDurably(job.isDurable())
                .usingJobData(jobDataMap)
                .build();
    }

    private CronTrigger createTrigger(ScheduleJob job) throws ParseException {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(job.getCronExpression())
                .withMisfireHandlingInstructionDoNothing();
        return TriggerBuilder.newTrigger()
                .forJob(job.getJobName(), job.getJobGroup())
                .withIdentity("trigger_" + job.getJobName(), job.getJobGroup())
                .withDescription(job.getDescription())
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    private boolean addJobToScheduler(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        if (overwriteExistingJobs || scheduler.checkExists(jobDetail.getKey())) {
            scheduler.addJob(jobDetail, true);
            return true;
        } else {
            return false;
        }
    }

    private boolean addTriggerToScheduler(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        boolean triggerExists = scheduler.checkExists(trigger.getKey());
        if (!triggerExists || overwriteExistingJobs) {
            if (!triggerExists) {
                try {
                    scheduler.scheduleJob(trigger);
                } catch (ObjectAlreadyExistsException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Unexpectedly found existing trigger, assumably due to cluster race condition: "
                                + ex.getMessage() + " - can safely be ignored");
                    }
                    if (overwriteExistingJobs) {
                        scheduler.rescheduleJob(trigger.getKey(), trigger);
                    }
                }
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
            return true;
        } else {
            return false;
        }
    }

}
