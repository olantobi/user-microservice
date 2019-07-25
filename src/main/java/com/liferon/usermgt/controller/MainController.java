/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import com.liferon.usermgt.exceptions.InvalidRequestParameterException;
import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.model.User;
import com.liferon.usermgt.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.liferon.usermgt.dto.AuditDto;
import com.liferon.usermgt.event.AuditTrailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author olanrewaju.ebenezer
 */
@RestController
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);
    
    @Autowired
    private UserService userService;    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @GetMapping("/user")
    public ResponseEntity<?> User(Authentication auth) {
        OAuth2Authentication oauth = (OAuth2Authentication) auth;
        
        Map userDetails = new HashMap();
        User user = userService.findByUsername(auth.getName());
        
        if (user != null)
            userDetails.put("tenant", (user.getTenant() == null) ? "" : user.getTenant().getTenantName());
        
        oauth.setDetails(userDetails);        
                        
        System.out.println(oauth);
        return ResponseEntity.ok(oauth);
    }
    
    @GetMapping("/permissions")    
    public ResponseEntity<List<Permission>> getPermissions(Authentication auth, HttpServletRequest request) {
        User currentUser = userService.findByUsername(auth.getName());
        Role role = currentUser.getRole();
        
        if (role == null) {           
            return new ResponseEntity<List<Permission>>(HttpStatus.NOT_FOUND);            
        }
        
        List<Permission> permissions = role.getPermissions();
        
        return new ResponseEntity<List<Permission>>(permissions, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Change Password", nickname = "Change My Password")    
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success - Password changed successfully", response = Long.class),
        @ApiResponse(code = 400, message = "Bad Request - Incorrect password"),
        @ApiResponse(code = 404, message = "User not found")
    })
    @RequestMapping(value = "/api/changePassword", method = RequestMethod.POST)
    public ResponseEntity<User> changePassword(@RequestParam("oldPassword") String oldPassword, 
            @RequestParam("newPassword") String newPassword, HttpServletRequest request) throws InvalidRequestParameterException {
        
        User currentUser = userService.findByUsername(request.getRemoteUser());
          
        if (currentUser==null) {            
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword()))              
            throw new InvalidRequestParameterException("Incorrect old password");
                             
        String encodedOldPassword = passwordEncoder.encode(oldPassword);
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        
        AuditDto audit = new AuditDto("Main", "changePassword", "UPDATE", encodedOldPassword, encodedNewPassword);
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        currentUser.setPassword(encodedNewPassword);        
        userService.update(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }
}
