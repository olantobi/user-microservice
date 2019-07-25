/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import com.liferon.usermgt.model.Permission;
import java.util.List;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface PermissionService {
    Permission getPermission(Long id);
    Permission getPermissionByKey(String permissionKey);
    Permission savePermission(Permission permission);
    Permission updatePermission(Permission permission);
    void deletePermission(Permission permission);
    void deletePermission(Long permissionId);
    List<Permission> getAll();
    List<Permission> getAllEnabled();
    boolean permissionExists(Permission permission);
    boolean permissionKeyExists(String permissionKey);
}
