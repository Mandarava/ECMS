package com.finance.activiti.entity;

import java.io.Serializable;
import java.util.List;

public class ResourceTaskDefinition implements Serializable {

    private static final long serialVersionUID = -4139628267696395837L;

    private String taskDefinitionId;

    private List<ResourceAuthority> resources;

    public String getTaskDefinitionId() {
        return taskDefinitionId;
    }

    public void setTaskDefinitionId(String taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    public List<ResourceAuthority> getResources() {
        return resources;
    }

    public void setResources(List<ResourceAuthority> resources) {
        this.resources = resources;
    }
}
