package com.finance.quartz;

import com.finance.exception.BusinessException;
import com.finance.quartz.quartz22.CustomQuartzJobBean;
import com.finance.quartz.quartz22.StatefulQuartzJobBean;
import com.finance.util.ApplicationContextUtil;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * Created by zt 2017/9/28 20:13
 */
public class DynamicScheduler {

    public static final String DATA_MAP_KEY_JOB = "scheduleJob";
    private static final boolean overwriteExistingJobs = true;
    private static final Logger logger = LoggerFactory.getLogger(DynamicScheduler.class);
    private static Scheduler schedulerCluster = (Scheduler) ApplicationContextUtil.getBean("quartzCluster");
    private static Scheduler schedulerLocal = (Scheduler) ApplicationContextUtil.getBean("quartzNonCluster");

    public static void addJob(ScheduleJob job) throws ParseException, SchedulerException {
        if (!validateScheduleJob(job)) {
            throw new BusinessException("add job failed, the job is invalid");
        }
        if (StringUtils.isEmpty(job.getTargetObject()) || StringUtils.isEmpty(job.getTargetMethod())) {
            throw new BusinessException("add job failed, target object or target method is empty");
        }
        Scheduler existScheduler = findScheduler(job.getTriggerName(), job.getJobGroup());
        if (existScheduler != null) {
            throw new BusinessException(String.format("add job failed, trigger: {}.{} is already existed", job.getJobGroup(), job.getTriggerName()));
        }
        Scheduler scheduler;
        if (job.isCluster()) {
            scheduler = schedulerCluster;
        } else {
            scheduler = schedulerLocal;
        }
        JobDetail jobDetail = createJobDetail(job);
        CronTrigger cronTrigger = createTrigger(job);
        addJobToScheduler(jobDetail, scheduler);
        addTriggerToScheduler(cronTrigger, scheduler);
        logger.info(">>>>>>>add job {} success in {} mode.", cronTrigger.getKey(), scheduler == schedulerCluster ? "cluster" : "local");
    }

    public static void deleteJob(ScheduleJob job) throws SchedulerException {
        if (!validateScheduleJob(job)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getJobGroup());
        Scheduler scheduler = findScheduler(triggerKey);
        if (scheduler == null) {
            throw new BusinessException("delete job failed, can not find the exist job either in cluster or local");
        }
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.deleteJob(jobKey);
        logger.info(">>>>>>>delete job {} success", jobKey);
    }

    public static void unScheduleJob(ScheduleJob job) throws SchedulerException {
        if (!validateScheduleJob(job)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getJobGroup());
        Scheduler scheduler = findScheduler(triggerKey);
        if (scheduler == null) {
            throw new BusinessException("unSchedule job failed, can not find the exist job either in cluster or local");
        }
        scheduler.unscheduleJob(triggerKey);
        logger.info(">>>>>>>unSchedule job {} success", triggerKey);
    }

    public static void triggerJob(ScheduleJob job) throws SchedulerException {
        if (!validateScheduleJob(job)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getJobGroup());
        Scheduler scheduler = findScheduler(triggerKey);
        if (scheduler == null) {
            throw new BusinessException("trigger job failed, can not find the exist job either in cluster or local");
        }
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.triggerJob(jobKey);
        logger.info(">>>>>>>trigger job {} success.", jobKey);
    }

    public static void rescheduleJob(ScheduleJob job) throws SchedulerException, ParseException {
        if (!validateScheduleJob(job)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getJobGroup());
        Scheduler scheduler = findScheduler(triggerKey);
        if (scheduler == null) {
            throw new BusinessException("reschedule job failed, can not find the exist job either in cluster or local");
        }
        CronTrigger cronTrigger = createTrigger(job);
        scheduler.rescheduleJob(cronTrigger.getKey(), cronTrigger);
        logger.info(">>>>>>>reschedule job {} success", cronTrigger.getKey());
    }

    public static void pauseTrigger(ScheduleJob job) throws SchedulerException {
        if (!validateScheduleJob(job)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getJobGroup());
        Scheduler scheduler = findScheduler(triggerKey);
        if (scheduler == null) {
            throw new BusinessException("pause trigger failed, can not find the exist trigger either in cluster or local");
        }
        scheduler.pauseTrigger(triggerKey);
        logger.info(">>>>>>>pause trigger {} success.", triggerKey);
    }

    public static void resumeTrigger(ScheduleJob job) throws SchedulerException {
        if (!validateScheduleJob(job)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getJobGroup());
        Scheduler scheduler = findScheduler(triggerKey);
        if (scheduler == null) {
            throw new BusinessException("resume trigger failed, can not find the exist trigger either in cluster or local");
        }
        scheduler.resumeTrigger(triggerKey);
        logger.info(">>>>>>>resume trigger {} success.", triggerKey);
    }

    public static Scheduler findScheduler(String triggerName, String triggerGroup) throws SchedulerException {
        if (StringUtils.isEmpty(triggerGroup) || StringUtils.isEmpty(triggerName)) {
            logger.debug("trigger group or trigger name can not be empty!");
            return null;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
        return findScheduler(triggerKey);
    }

    private static Scheduler findScheduler(TriggerKey triggerKey) throws SchedulerException {
        if (schedulerCluster.checkExists(triggerKey)) {
            return schedulerCluster;
        }
        if (schedulerLocal.checkExists(triggerKey)) {
            return schedulerLocal;
        }
        return null;
    }

    private static boolean validateScheduleJob(ScheduleJob job) {
        if (job == null) {
            logger.debug("job is invalid: job is null");
            return false;
        }
        if (StringUtils.isEmpty(job.getJobName()) || StringUtils.isEmpty(job.getJobGroup())) {
            logger.debug("job is invalid: job name or job group is empty");
            return false;
        }
        return true;
    }

    public static JobDetail createJobDetail(ScheduleJob job) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(DATA_MAP_KEY_JOB, job);
        return JobBuilder.newJob(job.isConcurrent() ? CustomQuartzJobBean.class : StatefulQuartzJobBean.class)
                .withIdentity(job.getJobName(), job.getJobGroup())
                .withDescription(job.getDescription())
                .requestRecovery(job.isRecovery())
                .storeDurably(job.isDurable())
                .usingJobData(jobDataMap)
                .build();
    }

    public static CronTrigger createTrigger(ScheduleJob job) throws ParseException {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(job.getCronExpression());
        if (CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW == job.getMisfireInstruction()) {
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else if (CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING == job.getMisfireInstruction()) {
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        } else if (Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY == job.getMisfireInstruction()) {
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        }
        return TriggerBuilder.newTrigger()
                .forJob(job.getJobName(), job.getJobGroup())
                .withIdentity(job.getTriggerName(), job.getJobGroup())
                .withDescription(job.getDescription())
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    public static boolean addJobToScheduler(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        if (overwriteExistingJobs || scheduler.checkExists(jobDetail.getKey())) {
            scheduler.addJob(jobDetail, true);
            return true;
        } else {
            return false;
        }
    }

    public static boolean addTriggerToScheduler(Trigger trigger, Scheduler scheduler) throws SchedulerException {
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

    /*    public static void addJobListener(ScheduleJob job, Scheduler scheduler) throws SchedulerException {
        String[] jobListenerNames = job.getJobListenerNames();
        if (jobListenerNames != null) {
            for (int i = 0; i < jobListenerNames.length; i++) {
                scheduler.getListenerManager().addJobListenerMatcher(jobListenerNames[i]
                        , KeyMatcher.keyEquals(new JobKey(job.getJobName(), job.getJobGroup())));
            }
        }
    }*/

}
