/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author olanrewaju.ebenezer
 */
@ApiIgnore
@RestController
public class TestUserController {    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
        
    @PostMapping("/encrypt-password")
    public String encrypt(@RequestParam("password") String plainPassword) {        
        return passwordEncoder.encode(plainPassword);
    }
    
    @GetMapping("/my-ip")
    public String myIP(HttpServletRequest request) {
        
        return request.getRemoteAddr();
    }    
}
