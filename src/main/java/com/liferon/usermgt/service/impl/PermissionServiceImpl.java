/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.PermissionRepository;
import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.service.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission getPermissionByKey(String permission) {
        return permissionRepository.findByPermissionKey(permission);
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }    

    @Override
    public Permission getPermission(Long permissionId) {
        return permissionRepository.findById(permissionId).orElse(null);
    }

    @Override
    public Permission updatePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Permission permission) {
        permissionRepository.delete(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }

    @Override
    public List<Permission> getAll() {        
            return permissionRepository.findAll();                
    }

    @Override
    public List<Permission> getAllEnabled() {
        return permissionRepository.findByEnabledTrue();        
    }
    
    @Override
    public boolean permissionExists(Permission permission) {
        return (permissionRepository.findByPermissionKey(permission.getPermissionKey()) != null);
    }

    @Override
    public boolean permissionKeyExists(String permissionKey) {
        return (permissionRepository.findByPermissionKey(permissionKey) != null);
    }
}
