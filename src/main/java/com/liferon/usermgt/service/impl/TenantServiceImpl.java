/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.TenantRepository;
import com.liferon.usermgt.model.Tenant;
import com.liferon.usermgt.service.TenantService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;
    
    @Override
    public Tenant getTenant(String tenantId) {
        return tenantRepository.findById(tenantId).orElse(null);
    }

    @Override
    public Tenant getTenantByName(String tenantName) {
        return tenantRepository.findByTenantName(tenantName);
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant updateTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @Override
    public void deleteTenant(Tenant tenant) {
        tenantRepository.delete(tenant);
    }

    @Override
    public void deleteTenant(String tenantId) {
        tenantRepository.deleteById(tenantId);
    }

    @Override
    public List<Tenant> getAll() {
        return tenantRepository.findAll();
    }

    @Override
    public boolean tenantExists(Tenant tenant) {
        return (tenantRepository.findByTenantName(tenant.getTenantName()) != null);
    }

    @Override
    public boolean tenantNameExists(String tenantName) {
        return (tenantRepository.findByTenantName(tenantName) != null);
    }
    
}
