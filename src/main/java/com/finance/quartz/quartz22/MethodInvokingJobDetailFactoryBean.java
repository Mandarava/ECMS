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
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.MethodInvoker;

public class MethodInvokingJobDetailFactoryBean implements FactoryBean, BeanNameAware, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(MethodInvokingJobDetailFactoryBean.class);

    private String beanName;

    private JobDetail jobDetail;

    private String group = Scheduler.DEFAULT_GROUP;

    private boolean concurrent = true;

    private boolean durable = false;

    private boolean shouldRecover = false;

    private Object targetObject;

    private String targetMethod;

    private String targetClass;

    private String staticMethod;

    private Object[] arguments;

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
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

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setShouldRecover(boolean shouldRecover) {
        this.shouldRecover = shouldRecover;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setStaticMethod(String staticMethod) {
        this.staticMethod = staticMethod;
    }

    public void afterPropertiesSet() throws Exception {
        try {
            logger.debug("start");
            logger.debug("Creating JobDetail " + beanName);
            jobDetail = JobBuilder.newJob(concurrent ? MethodInvokingJob.class : StatefulMethodInvokingJob.class)
                    .withIdentity(beanName, group)
                    .storeDurably(durable)
                    .requestRecovery(shouldRecover)
                    .build();
            if (targetClass != null)
                jobDetail.getJobDataMap().put("targetClass", targetClass);
            if (targetObject != null)
                jobDetail.getJobDataMap().put("targetObject", targetObject);
            if (targetMethod != null)
                jobDetail.getJobDataMap().put("targetMethod", targetMethod);
            if (staticMethod != null)
                jobDetail.getJobDataMap().put("staticMethod", staticMethod);
            if (arguments != null)
                jobDetail.getJobDataMap().put("arguments", arguments);

        } finally {
            logger.debug("end");
        }
    }

    public static class MethodInvokingJob implements Job {

        protected Logger logger = LoggerFactory.getLogger(MethodInvokingJob.class);

        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                logger.debug("start");
                String targetClass = context.getMergedJobDataMap().getString("targetClass");
                logger.debug("targetClass is " + targetClass);
                Class targetClassClass = null;
                if (targetClass != null) {
                    targetClassClass = Class.forName(targetClass); // Could throw ClassNotFoundException
                }
                Object targetObject = context.getMergedJobDataMap().get("targetObject");
                logger.debug("targetObject is " + targetObject);
                String targetMethod = context.getMergedJobDataMap().getString("targetMethod");
                logger.debug("targetMethod is " + targetMethod);
                String staticMethod = context.getMergedJobDataMap().getString("staticMethod");
                logger.debug("staticMethod is " + staticMethod);
                Object[] arguments = (Object[]) context.getMergedJobDataMap().get("arguments");
                logger.debug("arguments are " + arguments);

                logger.debug("creating MethodInvoker");
                MethodInvoker methodInvoker = new MethodInvoker();
                methodInvoker.setTargetClass(targetClassClass);
                methodInvoker.setTargetObject(targetObject);
                methodInvoker.setTargetMethod(targetMethod);
                methodInvoker.setStaticMethod(staticMethod);
                methodInvoker.setArguments(arguments);
                methodInvoker.prepare();
                logger.info("Invoking: " + methodInvoker.getPreparedMethod().toGenericString());
                context.setResult(methodInvoker.invoke());
            } catch (Exception e) {
                throw new JobExecutionException(e);
            } finally {
                logger.debug("end");
            }
        }
    }

    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class StatefulMethodInvokingJob extends MethodInvokingJob {
        // No additional functionality; just needs to implement StatefulJob.
    }


}