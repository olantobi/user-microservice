/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class PermissionDto {    
    @NotEmpty(message="Permission key is a required field")
    private String permissionKey;  
    private String description;
    private boolean enabled;
    
    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "PermissionDto{" + "permissionKey=" + permissionKey + ", description=" + description + ", enabled=" + enabled + '}';
    }               
}
