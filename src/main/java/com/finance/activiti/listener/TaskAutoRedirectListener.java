package com.finance.activiti.listener;

import org.activiti.engine.EngineServices;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TaskAutoRedirectListener implements TaskListener {

    private static final long serialVersionUID = -3607743798126951485L;
    private static final Logger logger = LoggerFactory.getLogger(TaskAutoRedirectListener.class);
    private static Map<String, String> userMap = new HashMap<>(); // TODO

    static {
        userMap.put("henryyan", "thomas");
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String originAssignee = delegateTask.getAssignee();
        String newUser = userMap.get(originAssignee); // 预设的转办人
        if (StringUtils.isNotEmpty(newUser)) {
            delegateTask.setAssignee(newUser);
            EngineServices engineServices = delegateTask.getExecution().getEngineServices();
            TaskService taskService = engineServices.getTaskService();
            String message = getClass().getName() + "-> 任务 [" + delegateTask.getName() + "]的办理人[" + originAssignee + "]的任务自动转办给了用户[" + newUser + "]";
            taskService.addComment(delegateTask.getId(), delegateTask.getProcessInstanceId(), "redirect", message);
        } else {
            logger.info("任务[" + delegateTask.getName() + "]没有预设的转办人");
        }

    }

}
