/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.component;



import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ebenezer
 */
//@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {
   
   @Override
   public void onApplicationEvent(AbstractAuthenticationEvent authenticationEvent) {
      if (authenticationEvent instanceof InteractiveAuthenticationSuccessEvent) {
         // ignores to prevent duplicate logging with AuthenticationSuccessEvent
         return;
      }
      Authentication authentication = authenticationEvent.getAuthentication();
      //String auditMessage = "Login attempt with username: " + authentication.getName() + "\t\tSuccess: " + authentication.isAuthenticated();
      if (authentication.isAuthenticated()) {
          //Principal prin = (Principal)authentication.getPrincipal();          
      }
      //System.out.println(auditMessage);
      //logger.info(auditMessage);
   }

}
