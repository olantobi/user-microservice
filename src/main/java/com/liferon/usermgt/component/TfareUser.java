/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.component;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class TfareUser extends UsernamePasswordAuthenticationToken {
    
    private String tenant;    

    public TfareUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String tenantName) {
        super(username, password, authorities);
        
        this.tenant = tenantName;
    }          

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
        
}
