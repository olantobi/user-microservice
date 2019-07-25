/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.liferon.usermgt.config.Auditable;
import com.liferon.usermgt.dto.PermissionDto;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Entity
public class Permission extends Auditable<String> {
    @Id
    //@GeneratedValue(generator = "UUID")
    //@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;    
    
    @NotEmpty
    @NotNull    
    @Column(name="permission_key", unique = true)
    private String permissionKey;    
    private String description;
        
    @JsonManagedReference
    @ManyToMany(mappedBy = "permissions")    
    private List<Role> roles;

    private boolean enabled;
    
    public Permission() {}
    
    public Permission(String permissionKey) {
        this.permissionKey = permissionKey;
    }
    
    public Permission(String permissionKey, String description) {
        this.permissionKey = permissionKey;
        this.description = description;
        this.enabled = true;
    }    
    
    public Permission(PermissionDto permissionDto) {
        this.permissionKey = permissionDto.getPermissionKey();
        this.description = permissionDto.getDescription();
        this.enabled = true;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.permissionKey);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Permission other = (Permission) obj;
        if (!Objects.equals(this.permissionKey, other.permissionKey)) {
            return false;
        }
        //if (!Objects.equals(this.id, other.id)) {
          //  return false;
        //}
        return true;
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
        return "Permission{" + "id=" + id + ", permissionKey=" + permissionKey + ", description=" + description + ",enabled=" + enabled +"'}'";
    }        
    
}
