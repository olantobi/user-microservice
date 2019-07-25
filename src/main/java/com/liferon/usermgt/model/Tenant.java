/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.model;

import com.liferon.usermgt.config.Auditable;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Ebenezer
 */
@Entity
public class Tenant extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = -4663885854555254745L;
    
    @Id    
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @Column(name="tenant_name")
    private String tenantName;        
    private boolean enabled;
    
    public Tenant() {        
    }
    
    public Tenant(String tenantName) {
        this.tenantName = tenantName;
    }
    
    public Tenant(String tenantId, String tenantName) {
        this.id = tenantId;
        this.tenantName = tenantName;
        this.enabled = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }        

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Tenant{" + "tenantId=" + id + ", tenantName=" + tenantName + ", enabled=" + enabled + '}';
    }    
}
