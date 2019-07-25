/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.repository;

import com.liferon.usermgt.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Component
public interface TenantRepository extends JpaRepository<Tenant, String> {
    Tenant findByTenantName(String tenantName);
}
