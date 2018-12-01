package com.finance.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class TaskAutoRedirectGlobalEventListener implements ActivitiEventListener {

    private static Map<String, String> userMap = new HashMap<>();

    static {
        userMap.put("henryyan", "thomas");
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        Object entity = entityEvent.getEntity();
        if (entity instanceof TaskEntity) {
            TaskEntity task = (TaskEntity) entity;
            String originUserId = task.getAssignee();
            String newUserId = userMap.get(originUserId);
            if (StringUtils.isNotEmpty(newUserId)) {
                task.setAssignee(newUserId);
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
