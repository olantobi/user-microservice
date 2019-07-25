/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.component;

import com.liferon.usermgt.service.AccessLogService;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author Ebenezer
 */
public final class LoginSuccessHandler implements AuthenticationSuccessHandler
{    
    @Autowired
    private AccessLogService accessLogService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {        
        String username = ((Principal)authentication.getPrincipal()).getName();
        accessLogService.logAccess(username, request.getRemoteAddr());
    }
}
