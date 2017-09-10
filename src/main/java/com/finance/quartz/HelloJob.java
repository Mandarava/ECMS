package com.finance.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zt 2017/9/5 22:08
 */
public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Time is " + simpleDateFormat.format(date) + " Hello World!");

        Trigger trigger = context.getTrigger();
        System.out.println("start time is " + trigger.getStartTime() + " end time is " + trigger.getEndTime());
        JobKey triggerJobKey = trigger.getJobKey();
        System.out.println("job key info " + triggerJobKey.getGroup() + ", " + triggerJobKey.getName() + ", " + triggerJobKey.getClass());

        JobKey jobKey = context.getJobDetail().getKey();
        System.out.println("job key name and group is " + jobKey.getName() + ", " + jobKey.getGroup());
        TriggerKey triggerKey = context.getTrigger().getKey();
        System.out.println("trigger key name and group is " + triggerKey.getName() + ", " + triggerKey.getGroup());

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobDataMap triggerDataMap = context.getTrigger().getJobDataMap();
//        JobDataMap dataMap = context.getMergedJobDataMap();
        String jobMessage = jobDataMap.getString("message");
        String triggerMessage = triggerDataMap.getString("message");
        System.out.println("job message is " + jobMessage);
        System.out.println("trigger message is " + triggerMessage);
    }
}
