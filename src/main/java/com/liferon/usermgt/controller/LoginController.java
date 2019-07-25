/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import java.security.Principal;
import com.liferon.usermgt.model.AccessLog;
import com.liferon.usermgt.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Controller
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private AccessLogService accessLogService;
    //@Autowired
    //private UserService userService;
    
    @RequestMapping("/loginHistory")
    @PreAuthorize("hasPermission(#permission, 'can_view_loginHistories') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<?> getAllLoginHistory(Principal principal, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="pageSize", defaultValue="10") int pageSize) {

        page = (page <= 0) ? 0 : page-1;
        
        Pageable pageable = new PageRequest(page, pageSize, Direction.DESC, "id");
        Page<AccessLog> accessLogs = accessLogService.getAccessLogs(principal.getName(), pageable);
        
        return ResponseEntity.ok(accessLogs.getContent());        
    }
    
    @RequestMapping("/lastLogin")    
    public ResponseEntity<?> getLastLogin(Principal principal) {                
        AccessLog accessLog = accessLogService.getLastLogin(principal.getName());
        
        return ResponseEntity.ok(accessLog);
    }
}
