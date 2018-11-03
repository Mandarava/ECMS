package com.finance.activiti.entity;

import java.io.Serializable;
import java.util.List;

public class ResourceAuthorities implements Serializable {

    private static final long serialVersionUID = 7547863445930478112L;

    private List<ResourceAuthority> resourceAuthority;

    public List<ResourceAuthority> getResourceAuthority() {
        return resourceAuthority;
    }

    public void setResourceAuthority(List<ResourceAuthority> resourceAuthority) {
        this.resourceAuthority = resourceAuthority;
    }
}
