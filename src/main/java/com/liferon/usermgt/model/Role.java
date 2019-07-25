/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.liferon.usermgt.config.Auditable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Ebenezer Olanrewaju
 */
@Entity
public class Role extends Auditable<String> {
    @Id    
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name="role_name")
    private String roleName;    
    private boolean enabled;   
    @ManyToOne
    @JsonBackReference
    private Tenant tenant;    
    @JsonBackReference
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY) 
    private List<User> users;
     
    @JsonBackReference    
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "roles_permissions", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id", insertable=false, updatable=false), 
        inverseJoinColumns = @JoinColumn(
          name = "permission_id", referencedColumnName = "id", insertable=false, updatable=false))
    private List<Permission> permissions;
    
    public Role(){}
    
    public Role(String roleName){
        this.roleName = roleName;
        this.enabled = true;
    }
    
    public String getId() {
        return id;
    }

    public void seId(String Id) {
        this.id = Id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public void addPermission(Permission permission) {
        if (permissions == null)
            permissions = new ArrayList<Permission>();
        this.permissions.add(permission);
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
        return "Role{" + "id=" + id + ", roleName=" + roleName + ", enabled=" + enabled + ", tenant=" + tenant.getTenantName() + '}';
    }        
}
