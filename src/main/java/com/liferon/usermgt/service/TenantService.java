/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import com.liferon.usermgt.model.Tenant;
import java.util.List;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface TenantService {
    Tenant getTenant(String tenantId);
    Tenant getTenantByName(String tenantName);
    Tenant saveTenant(Tenant tenant);
    Tenant updateTenant(Tenant tenant);
    void deleteTenant(Tenant tenant);
    void deleteTenant(String tenantId);
    List<Tenant> getAll();
    boolean tenantExists(Tenant tenant);
    boolean tenantNameExists(String tenantName);
}
