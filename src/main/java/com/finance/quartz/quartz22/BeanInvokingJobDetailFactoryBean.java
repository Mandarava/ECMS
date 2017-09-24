package com.finance.quartz.quartz22;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.MethodInvoker;

public class BeanInvokingJobDetailFactoryBean implements FactoryBean, BeanNameAware, InitializingBean, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(BeanInvokingJobDetailFactoryBean.class);

    protected static ApplicationContext applicationContext;

    private JobDetail jobDetail;

    private String beanName;

    private String group = Scheduler.DEFAULT_GROUP;

    private String targetBean;

    private String targetMethod;

    private boolean concurrent = true;

    private boolean durable = false;

    private boolean shouldRecover = false;

    private String description;

    private Object[] arguments;

    public void setTargetBean(String targetBean) {
        this.targetBean = targetBean;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public void setShouldRecover(boolean shouldRecover) {
        this.shouldRecover = shouldRecover;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Object getObject() throws Exception {
        return jobDetail;
    }

    @Override
    public Class getObjectType() {
        return JobDetail.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public void afterPropertiesSet() throws Exception {
        try {
            logger.debug("start");
            logger.debug("Creating JobDetail " + beanName);
            jobDetail = JobBuilder.newJob(concurrent ? BeanInvokingJob.class : StatefulBeanInvokingJob.class)
                    .withIdentity(beanName, group)
                    .storeDurably(durable)
                    .requestRecovery(shouldRecover)
                    .withDescription(description == null ? targetBean + "." + targetMethod : description)
                    .build();
            jobDetail.getJobDataMap().put("targetBean", targetBean);
            jobDetail.getJobDataMap().put("targetMethod", targetMethod);
            jobDetail.getJobDataMap().put("arguments", arguments);
        } finally {
            logger.debug("end");
        }
    }

    public static class BeanInvokingJob implements Job {

        protected Logger logger = LoggerFactory.getLogger(BeanInvokingJob.class);

        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                logger.debug("start");

                String targetBean = context.getMergedJobDataMap().getString("targetBean");
                logger.debug("targetBean is " + targetBean);
                if (targetBean == null)
                    throw new JobExecutionException("targetBean cannot be null.", false);

                String targetMethod = context.getMergedJobDataMap().getString("targetMethod");
                logger.debug("targetMethod is " + targetMethod);
                if (targetMethod == null)
                    throw new JobExecutionException("targetMethod cannot be null.", false);

                // when org.quartz.jobStore.useProperties=="true" the arguments entry (which should be an Object[]) in the JobDataMap gets converted into a String.
                Object argumentsObject = context.getMergedJobDataMap().get("arguments");
                Object[] arguments = (argumentsObject instanceof String) ? null : (Object[]) argumentsObject;
                logger.debug("arguments array is " + arguments);

                Object bean = applicationContext.getBean(targetBean);
                logger.debug("applicationContext resolved bean name/id '" + targetBean + "' to " + bean);

                MethodInvoker beanMethod = new MethodInvoker();
                beanMethod.setTargetObject(bean);
                beanMethod.setTargetMethod(targetMethod);
                beanMethod.setArguments(arguments);
                beanMethod.prepare();
                logger.info("Invoking Bean: " + targetBean + "; Method: " + targetMethod + "; arguments: " + arguments + ";");
                context.setResult(beanMethod.invoke());
            } catch (JobExecutionException e) {
                throw e;
            } catch (Exception e) {
                throw new JobExecutionException(e);
            } finally {
                logger.debug("end");
            }
        }
    }

    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class StatefulBeanInvokingJob extends BeanInvokingJob {
        // No additional functionality; just needs to implement StatefulJob.
    }
}