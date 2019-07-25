/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import com.liferon.usermgt.model.Role;
import java.util.List;

/**
 *
 * @author Ebenezer
 */
public interface RoleService {
    Role getRole(String roleId);
    Role getRoleByName(String roleName);
    Role saveRole(Role role);
    Role updateRole(Role role);
    void deleteRole(Role role);
    void deleteRole(String roleId);
    List<Role> getAll();
    List<Role> getAllEnabled();
    boolean roleExists(Role role);
    boolean roleNameExists(String roleName);
}
