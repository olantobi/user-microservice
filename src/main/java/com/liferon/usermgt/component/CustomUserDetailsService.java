/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.component;

import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.model.User;
import com.liferon.usermgt.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ebenezer
 */
//@Service("userDetailsService")
public class CustomUserDetailsService //implements UserDetailsService{
{ 
    @Autowired
    private UserService userService;
     
    @Transactional(readOnly=true)
    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user==null){
            throw new UsernameNotFoundException("Username not found");
        }
        //System.out.println(user);
        //return new AseduUser(user.getUsername(), user.getPassword(), getGrantedAuthorities(user), user.getTenant().getTenantName());
        return new org.springframework.security.core.userdetails.User(username, username, getGrantedAuthorities(user));
    }
 
     
    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = user.getRole();
        authorities.clear();
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        return authorities;
    }
     
}
