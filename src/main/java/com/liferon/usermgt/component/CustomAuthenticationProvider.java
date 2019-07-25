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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ebenezer
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
 
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        User user = userService.findByUsername(username);
        
        if (user == null) {            
            throw new UsernameNotFoundException("Username not found");
        }
        
        if (passwordEncoder.matches(password, user.getPassword())) {                            
            //return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(user));
            return new TfareUser(username, password, getGrantedAuthorities(user), (user.getTenant() == null) ? "" : user.getTenant().getTenantName());
        } else {
            throw new MyAuthenticationException("Unable to verify username and password");
        }
    }
 
    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = user.getRole();
        authorities.clear();        
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        if (user.getUsername().equals("sunnyben")) {            
           // authorities.add(new SimpleGrantedAuthority("#oauth2.hasScope('can_view_users')"));
        }
        /* 
        for(UserProfile userProfile : user.getUserProfiles()){
            System.out.println("UserProfile : "+userProfile);
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getType()));
        }
                */
        //System.out.print("authorities :"+authorities);
        return authorities;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    class MyAuthenticationException extends AuthenticationException {

        public MyAuthenticationException(String msg) {
            super(msg);
        }
        
    }
}
