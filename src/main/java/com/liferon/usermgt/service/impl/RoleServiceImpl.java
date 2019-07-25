/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.RoleRepository;
import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.service.RoleService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ebenezer
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String role) {
        return roleRepository.getByRoleName(role).get();
    }

    @Override
    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRole(String roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getAllEnabled() {
        return roleRepository.findByEnabledTrue();
    }

    @Override
    public boolean roleExists(Role role) {
        return roleNameExists(role.getRoleName());
    }

    @Override
    public boolean roleNameExists(String roleName) {
        return roleRepository.getByRoleName(roleName).isPresent();
    }
}
