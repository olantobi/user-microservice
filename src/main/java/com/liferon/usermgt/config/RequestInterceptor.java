/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.config;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Ebenezer
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${AUDIT_URL}")
    private String auditTrailUrl;
            
    private Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
            
        if (auditTrailUrl != null && !auditTrailUrl.isEmpty()) {
            URI uri = URI.create(auditTrailUrl);
            
            String authHeader = request.getHeader("Authorization"); 
            System.out.println("Auth header: "+authHeader);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", authHeader);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("moduleName", "USER MANAGEMENT");
            map.add("actionName", "Index");
            map.add("sectionName", "");
            map.add("sourceIp", request.getRemoteAddr());
            map.add("details", request.getMethod()+ " " +request.getRequestURI());
            map.add("sourceDevice", "");

            HttpEntity<MultiValueMap<String, String>> message = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            
            try {
                System.out.println("Sending message to audit trail service");
                ResponseEntity<String> restponse = restTemplate.exchange(uri, HttpMethod.POST, message, String.class);                
                System.out.println("Audit response: "+restponse.getStatusCode());
            } catch (Exception e) {
                System.out.println("Unable to send message to audit trail service");
            }
        } else {
            System.out.println("Audit url is empty");
        }
        return true;
    }
}
