/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.security;

import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.service.RoleService;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private RoleService roleService;
    
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {       
        if (!(permission instanceof String))
            return false;
        
        return userHasPermission(auth, permission.toString().toLowerCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        System.out.println("Auth: "+auth+" :: Target ID: "+targetId+" :: Target Type: "+targetType+" :: Permission: "+permission);
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType)) {
                if (grantedAuth.getAuthority().contains(permission)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean userHasPermission(Authentication auth, String permission) {        
        if (auth.getAuthorities().size() <= 0)
            return false;                
        
        String roleName = auth.getAuthorities().iterator().next().toString();        
        
        Role role = roleService.getRoleByName(roleName);

        if (role.getPermissions().contains(new Permission(permission)))
            return true;
        
        return false;
    }
}
