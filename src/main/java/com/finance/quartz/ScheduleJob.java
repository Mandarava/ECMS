package com.finance.quartz;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zt 2017/9/21 21:02
 */
@Getter
@Setter
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

}
