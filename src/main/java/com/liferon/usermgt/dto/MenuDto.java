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
public class MenuDto {
    @NotEmpty(message = "Menu name is a required field")
    private String menuName;    
    private String menuUrl;
    @NotEmpty(message = "Permission key is a required field")
    private String permissionKey;
    private boolean enabled=true;
    private long parentMenuId;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(long parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    @Override
    public String toString() {
        return "MenuDto{" + "menuName=" + menuName + ", menuUrl=" + menuUrl + ", permissionKey=" + permissionKey + ", enabled=" + enabled + ", parentMenuId=" + parentMenuId + '}';
    }               
}
