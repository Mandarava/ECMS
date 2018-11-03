package com.finance.activiti.entity;

import java.io.Serializable;
import java.util.List;

public class ResourceProcessDefinition implements Serializable {

    private static final long serialVersionUID = -8994410807953454659L;

    private String processDefinitionId;

    private List<ResourceTaskDefinition> taskDefinitions;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public List<ResourceTaskDefinition> getTaskDefinitions() {
        return taskDefinitions;
    }

    public void setTaskDefinitions(List<ResourceTaskDefinition> taskDefinitions) {
        this.taskDefinitions = taskDefinitions;
    }
}
