/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.event;

import javax.servlet.http.HttpServletRequest;
import com.liferon.usermgt.dto.AuditDto;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class AuditTrailEvent extends ApplicationEvent {
    private AuditDto auditDto;
    private HttpServletRequest request;
    
    public AuditTrailEvent(AuditDto auditDto, HttpServletRequest request) {
        super(auditDto);
        this.auditDto = auditDto;
        this.request = request;        
    }

    public AuditDto getAuditDto() {
        return auditDto;
    }

    public void setAuditDto(AuditDto auditDto) {
        this.auditDto = auditDto;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }        
    
}
