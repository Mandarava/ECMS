package com.finance.quartz.listener;

import org.quartz.SchedulerException;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zt 2017/9/23 15:48
 */
public class QuartzExceptionSchedulerListener extends SchedulerListenerSupport {

    private Logger logger = LoggerFactory.getLogger(QuartzExceptionSchedulerListener.class);

    @Override
    public void schedulerError(String message, SchedulerException e) {
        super.schedulerError(message, e);
        logger.error(message, e.getUnderlyingException());
    }

}
