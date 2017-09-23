package com.finance.quartz.quartz22;

import com.finance.quartz.ScheduleJob;
import com.finance.util.ApplicationContextUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.MethodInvoker;


/**
 * Created by zt 2017/9/21 21:06
 */
public class CustomQuartzJobBean extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(CustomQuartzJobBean.class);

    private ScheduleJob scheduleJob;

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("start");

            String targetBean = scheduleJob.getTargetObject();
            logger.debug("targetBean is " + targetBean);
            if (targetBean == null)
                throw new JobExecutionException("targetBean cannot be null.", false);

            String targetMethod = scheduleJob.getTargetMethod();
            logger.debug("targetMethod is " + targetMethod);
            if (targetMethod == null)
                throw new JobExecutionException("targetMethod cannot be null.", false);

            // when org.quartz.jobStore.useProperties=="true" the arguments entry (which should be an Object[]) in the JobDataMap gets converted into a String.
            Object argumentsObject = scheduleJob.getArguments();
            Object[] arguments = (argumentsObject instanceof String) ? null : (Object[]) argumentsObject;
            logger.debug("arguments array is " + arguments);

            Object targetObject = ApplicationContextUtil.getBean(targetBean);

            MethodInvoker beanMethod = new MethodInvoker();
            beanMethod.setTargetObject(targetObject);
            beanMethod.setTargetMethod(targetMethod);
            beanMethod.setArguments(arguments);
            beanMethod.prepare();
            context.setResult(beanMethod.invoke());
            logger.debug("Invoking Bean: " + targetBean + "; Method: " + targetMethod + "; arguments: " + arguments + ";");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JobExecutionException(e);
        } finally {
            logger.debug("end");
        }
    }

    public void setScheduleJob(ScheduleJob scheduleJob) {
        this.scheduleJob = scheduleJob;
    }

}


