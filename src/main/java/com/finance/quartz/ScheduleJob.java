package com.finance.quartz;

import java.io.Serializable;

/**
 * Created by zt 2017/9/21 21:02
 */
public class ScheduleJob implements Serializable {

    /**
     * 任务 ID
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 时间表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String description;

    /**
     * Spring 注入的类名
     */
    private String targetObject;

    /**
     * 方法
     */
    private String targetMethod;

    /**
     * misfire后的处理方式
     */
    private String misfireInstruction;

    /**
     * 是否恢复
     */
    private boolean recovery;

    /**
     * durable
     */
    private boolean durable;

    /**
     * 是否并发
     */
    private boolean concurrent;

    /**
     * 是否集群运行
     */
    private boolean cluster;

    private Object[] arguments;

    private String[] jobListenerNames;

    public String getTriggerName() {
        return jobName == null ? "" : "trigger_" + jobName;
    }

    public String getJobName() {
        return jobName == null ? "" : "job_" + jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(String misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    public boolean isRecovery() {
        return recovery;
    }

    public void setRecovery(boolean recovery) {
        this.recovery = recovery;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public boolean isCluster() {
        return cluster;
    }

    public void setCluster(boolean cluster) {
        this.cluster = cluster;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String[] getJobListenerNames() {
        return jobListenerNames;
    }

    public void setJobListenerNames(String[] jobListenerNames) {
        this.jobListenerNames = jobListenerNames;
    }
}
