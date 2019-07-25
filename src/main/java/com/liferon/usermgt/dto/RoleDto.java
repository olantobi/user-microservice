/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class RoleDto {
    private String id;
    @NotEmpty(message="Role name is a required field")
    private String roleName;
    private boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "RoleDto{" + "id=" + id + ", roleName=" + roleName + ", enabled=" + enabled + '}';
    }        
}
