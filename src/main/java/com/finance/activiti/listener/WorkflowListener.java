package com.finance.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("workflowListener")
public class WorkflowListener implements ActivitiEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowListener.class);

    @Autowired
    private WorkflowTaskCreateListener workflowTaskCreateListener;

    @Autowired
    private WorkflowProcessEndListener workflowProcessEndListener;

    @Override
    @Transactional
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case TASK_CREATED:
                workflowTaskCreateListener.onEvent(event);
                break;
            case PROCESS_COMPLETED:
                workflowProcessEndListener.onEvent(event);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
