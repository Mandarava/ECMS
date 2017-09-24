package com.finance.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zt 2017/9/24 18:35
 */
@Component
public class QuartzJobListener extends JobListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobListener.class);

    @Override
    public String getName() {
        return "QuartzJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Job to be executed!");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>job execution vetoed!");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>job was executed!");
    }
}
