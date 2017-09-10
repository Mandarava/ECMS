package com.finance.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zt 2017/9/5 22:11
 */
public class HelloScheduler {

    public static void main(String[] args) throws SchedulerException {
//        testSimpleTrigger();
        testCronTrigger();
    }

    private static void testSimpleTrigger() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(HelloJob.class)
                .withIdentity("myJob", "group1")
                .usingJobData("message", "hello myJob1")
                .build();
        System.out.println("jonDetail's name : " + jobDetail.getKey().getName()
                + "  jonDetail's group : " + jobDetail.getKey().getGroup()
                + "  jonDetail's class name : " + jobDetail.getJobClass().getName());

        Date endDate = new Date();
        endDate.setTime(endDate.getTime() + 7000L);
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger", "group1")
                .usingJobData("message", "hello myTrigger1")
                .startNow() // .startAt(new Date());
                .endAt(endDate)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).withRepeatCount(5))
                .build();

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        System.out.println("Current Time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private static void testCronTrigger() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(HelloJob.class)
                .withIdentity("myJob", "group1")
                .build();

        Date endDate = new Date();
        endDate.setTime(endDate.getTime() + 7000L);
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ? *"))
                .build();

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        System.out.println("Current Time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
