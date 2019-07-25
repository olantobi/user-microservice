/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.config;

import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.DefaultSecurityContextAccessor;
import org.springframework.security.oauth2.provider.SecurityContextAccessor;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

/**
 *
 * @author olanrewaju.ebenezer
 */
//@Configuration
public class ScopeMappingRequestFactory extends DefaultOAuth2RequestFactory {    

    private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();

    public ScopeMappingRequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
        super.setCheckUserScopes(true);
    }

    /**
     * @param securityContextAccessor the security context accessor to set
     */
    @Override
    public void setSecurityContextAccessor(SecurityContextAccessor securityContextAccessor) {
        this.securityContextAccessor = securityContextAccessor;
        super.setSecurityContextAccessor(securityContextAccessor);
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
        AuthorizationRequest request = super.createAuthorizationRequest(authorizationParameters);

        if (securityContextAccessor.isUser()) {
            request.setAuthorities(securityContextAccessor.getAuthorities());
        }

        return request;
    }

}
