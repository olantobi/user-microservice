/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import com.liferon.usermgt.exceptions.ConflictException;
import com.liferon.usermgt.exceptions.InvalidRequestParameterException;
import com.liferon.usermgt.dto.AuditDto;
import com.liferon.usermgt.dto.TenantDto;
import com.liferon.usermgt.model.Tenant;
import com.liferon.usermgt.service.TenantService;
import java.util.List;
import javassist.NotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.liferon.usermgt.event.AuditTrailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author olanrewaju.ebenezer
 */
@RestController
@RequestMapping("/api")
public class TenantController {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @RequestMapping(value = "/tenant", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#tenant, 'can_view_tenants') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<List<Tenant>> listAllTenants(HttpServletRequest request) {
        List<Tenant> tenants = tenantService.getAll();
        if(tenants.isEmpty()){
            return new ResponseEntity<List<Tenant>>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<List<Tenant>>(tenants, HttpStatus.OK);
    }
      
    @RequestMapping(value = "/tenant/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#tenant, 'can_view_tenant') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Tenant> getTenant(@PathVariable("id") String id, HttpServletRequest request) {        
        Tenant tenant = tenantService.getTenant(id);
        if (tenant == null) {            
            return new ResponseEntity<Tenant>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<Tenant>(tenant, HttpStatus.OK);
    }
        
    @RequestMapping(value = "/tenant", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#tenant, 'can_create_tenant') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Void> createTenant(@RequestParam("tenant") String tenantName, UriComponentsBuilder ucBuilder,
            HttpServletRequest request) throws InvalidRequestParameterException, ConflictException {
        
        if (tenantName == null || tenantName.isEmpty()) {
            throw new InvalidRequestParameterException("Bad Request", "Tenant name cannot be empty");
        } 
        
        if (tenantService.tenantNameExists(tenantName)) {                        
            throw new ConflictException("Conflict", "A tenant with name "+tenantName+" already exists");
        }
        
        Tenant tenant = new Tenant(tenantName);
        tenant.setEnabled(true);
        
        Tenant createdTenant = tenantService.saveTenant(tenant);
  
        AuditDto audit = new AuditDto("Tenant", "createTenant", "CREATE", "", createdTenant.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/tenant/{id}").buildAndExpand(createdTenant.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }      
      
    @RequestMapping(value = "/tenant/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#tenant, 'can_update_tenant') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Tenant> updateTenant(@PathVariable("id") String id, @Valid @ModelAttribute TenantDto tenantDto,
            BindingResult result, HttpServletRequest request) throws NotFoundException {
        
        Tenant currentTenant = tenantService.getTenant(id);
          
        if (currentTenant == null) {            
            throw new NotFoundException("Tenant with "+id +" does not exist");
        }
        
        currentTenant.setTenantName(tenantDto.getTenantName());
        currentTenant.setEnabled(tenantDto.isEnabled());            
          
        Tenant newTenant = tenantService.saveTenant(currentTenant);
        
        AuditDto audit = new AuditDto("Tenant", "updateTenant", "UPDATE", currentTenant.toString(), newTenant.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Tenant>(newTenant, HttpStatus.OK);
    }
    /*
    @RequestMapping(value = "/tenant/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#tenant, 'can_delete_tenant') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Tenant> disableTenant(@PathVariable("id") String id, HttpServletRequest request) {          
        Tenant tenant = tenantService.getTenant(id);
        if (tenant == null) {            
            return new ResponseEntity<Tenant>(HttpStatus.NOT_FOUND);
        }
        
        AuditDto audit = new AuditDto("Tenant", "disableTenant", "Disable tenant with id: "+id, "");
        auditTrail.sendMessage(request, audit);
        
        tenant.setEnabled(false);
        tenantService.updateTenant(tenant);
        return new ResponseEntity<Tenant>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/tenant/{id}/enable", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#tenant, 'can_update_tenant') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Tenant> enableTenant(@PathVariable("id") String id, HttpServletRequest request) {        
  
        Tenant tenant = tenantService.getTenant(id);
        if (tenant == null) {            
            return new ResponseEntity<Tenant>(HttpStatus.NOT_FOUND);
        }
        
        AuditDto audit = new AuditDto("Tenant", "enableTenant", "Enable tenant with id: "+id, "");
        auditTrail.sendMessage(request, audit);
        
        tenant.setEnabled(true);
        tenantService.updateTenant(tenant);
        return new ResponseEntity<Tenant>(HttpStatus.NO_CONTENT);
    }
    /*
    @RequestMapping(value = "/tenant/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Tenant> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Tanent with id " + id);
  
        Tenant tenant = tenantService.getTenant(id);
        if (tenant == null) {
            System.out.println("Unable to delete. Tenant with id " + id + " not found");
            return new ResponseEntity<Tenant>(HttpStatus.NOT_FOUND);
        }
  
        tenantService.deleteTenant(tenant);
        return new ResponseEntity<Tenant>(HttpStatus.NO_CONTENT);
    }
*/
}
