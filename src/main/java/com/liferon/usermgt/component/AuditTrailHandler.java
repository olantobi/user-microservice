/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.component;

import java.net.URI;
import com.liferon.usermgt.event.AuditTrailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Component
public class AuditTrailHandler {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${AUDIT_URL}")
    private String auditTrailUrl;
    
    @Async
    @EventListener
    public void sendMessage(AuditTrailEvent auditEvent) {
        if (auditTrailUrl != null && !auditTrailUrl.isEmpty()) {
            URI uri = URI.create(auditTrailUrl);
            
            String authHeader = auditEvent.getRequest().getHeader("Authorization");             
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", authHeader);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("moduleName", "CONFIGURATION");
            map.add("actionName", auditEvent.getAuditDto().getActionName());
            map.add("actionType", auditEvent.getAuditDto().getActionType());
            map.add("sectionName", auditEvent.getAuditDto().getSectionName());            
            map.add("oldRecord", auditEvent.getAuditDto().getOldRecord());
            map.add("newRecord", auditEvent.getAuditDto().getNewRecord());
            map.add("details", auditEvent.getAuditDto().getDetails());
            map.add("sourceIp", auditEvent.getRequest().getRemoteAddr());
            //map.add("sourceDevice", auditEvent.getAuditDto().getSourceDevice());

            HttpEntity<MultiValueMap<String, String>> message = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            
            try {                
                ResponseEntity<String> restponse = restTemplate.exchange(uri+"/api/log", HttpMethod.POST, message, String.class);                 
            } catch (Exception e) {
                System.out.println("Unable to send message to audit trail service");
            }
        } else {
            System.out.println("Audit url is empty");
        }
    }
}
