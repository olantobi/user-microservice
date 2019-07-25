/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.repository;

import com.liferon.usermgt.model.Permission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByPermissionKey(String permissionKey);
    List<Permission> findByEnabledTrue();
    
    
}
