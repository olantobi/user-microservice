 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import com.liferon.usermgt.exceptions.ConflictException;
import com.liferon.usermgt.exceptions.InvalidRequestParameterException;
import com.liferon.usermgt.dto.AuditDto;
import com.liferon.usermgt.dto.PermissionDto;
import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.model.User;
import com.liferon.usermgt.service.PermissionService;
import com.liferon.usermgt.service.UserService;
import java.security.Principal;
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
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#permission, 'can_view_permissions') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<List<Permission>> listAllPermissions(HttpServletRequest request) {
        List<Permission> permissions = permissionService.getAllEnabled();
        if(permissions.isEmpty()){
            return new ResponseEntity<List<Permission>>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<List<Permission>>(permissions, HttpStatus.OK);
    }
      
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#permission, 'can_view_permission') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Permission> getPermission(@PathVariable("id") long id, HttpServletRequest request) {        
        Permission permission = permissionService.getPermission(id);
        if (permission == null) {            
            return new ResponseEntity<Permission>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<Permission>(permission, HttpStatus.OK);
    }
        
    @RequestMapping(value = "/permission", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#permission, 'can_create_permission') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Void> createPermission(@RequestParam("permissionKey") String permissionKey, @RequestParam("description") String description, 
            UriComponentsBuilder ucBuilder, HttpServletRequest request) throws InvalidRequestParameterException, ConflictException {
        
        if (permissionKey == null || permissionKey.isEmpty()) {
            throw new InvalidRequestParameterException("Bad Request", "Permission name cannot be empty");
        } 
        
        if (permissionService.permissionKeyExists(permissionKey)) {            
            throw new ConflictException("Conflict", "Permission key "+permissionKey+" already exists");
        }   
        
        Permission permission = new Permission(permissionKey, description);        
        permission.setEnabled(true);    
        
        Permission createdPermission = permissionService.savePermission(permission);
        
        AuditDto audit = new AuditDto("Permission", "createPermission", "CREATE", "", createdPermission.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/permission/{id}").buildAndExpand(createdPermission.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }      
      /*
    @RequestMapping(value = "/permissions", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#permission, 'can_create_permissions')")
    public ResponseEntity<Void> createPermissions(@RequestBody List<Permission> permissions, @RequestParam("tenantId") String tenantId, 
            UriComponentsBuilder ucBuilder, HttpServletRequest request) {
        
        if (permissions.isEmpty()) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        } 
        
        AuditDto audit = new AuditDto("Permission", "createPermissions", "Create multiple permissions on tenant "+tenantId, "");
        auditTrail.sendMessage(request, audit);
        
        /*
        if (permissionService.permissionNameExists(permissionName)) {
            //System.out.println("A permission with name " + permissionName+ " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }   
        
        if (tenantId == null)
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        
        Tenant tenant = tenantService.getTenant(tenantId);            
        if (tenant == null) {            
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        
        Permission permission = new Permission(permissionName, description);        
        permission.setEnabled(true);
        permission.setTenant(tenant);        
        *
        //Permission createdPermission = permissionService.savePermission(permission);
        
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/permission/{id}").buildAndExpand(createdPermission.getId()).toUri());
        //return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);    
    } 
    */
    
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#permission, 'can_update_permission') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Permission> updatePermission(@PathVariable("id") long id, @Valid @ModelAttribute PermissionDto permissionDto,
            HttpServletRequest request) throws InvalidRequestParameterException, NotFoundException { 
        
        Permission currentPermission = permissionService.getPermission(id);
          
        if (currentPermission==null) {            
            throw new NotFoundException("Permission with id "+id+" does not exist");
        }
        
        if (permissionDto.getPermissionKey() == null || permissionDto.getPermissionKey().isEmpty()) {
            throw new InvalidRequestParameterException("Bad Request", "Permission key cannot be empty");
        }                 
        
        currentPermission.setPermissionKey(permissionDto.getPermissionKey());
        currentPermission.setDescription(permissionDto.getDescription());            
          
        Permission newPermission = permissionService.savePermission(currentPermission);
        
        AuditDto audit = new AuditDto("Permission", "updatePermission", "UPDATE", currentPermission.toString(), newPermission.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Permission>(newPermission, HttpStatus.OK);
    }
      
    /*
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Permission> deletePermission(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Permission with id " + id);
  
        Permission permission = permissionService.getPermission(id);
        if (permission == null) {
            System.out.println("Unable to delete. Permission with id " + id + " not found");
            return new ResponseEntity<Permission>(HttpStatus.NOT_FOUND);
        }
  
        permissionService.deletePermission(permission);
        return new ResponseEntity<Permission>(HttpStatus.NO_CONTENT);
    }
    */
    
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#permission, 'can_delete_permission') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Permission> deletePermission(@PathVariable("id") long id, HttpServletRequest request) {          
        Permission permission = permissionService.getPermission(id);
        if (permission == null) {            
            return new ResponseEntity<Permission>(HttpStatus.NOT_FOUND);
        }
                
        permissionService.deletePermission(permission);
        
        AuditDto audit = new AuditDto("Permission", "deleteMenu", "DELETE", permission.toString(), "");
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Permission>(HttpStatus.OK);
    }
/*
    @RequestMapping(value = "/permission/{id}/enable", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#permission, 'can_update_permission') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Permission> enablePermission(@PathVariable("id") long id, HttpServletRequest request) {          
        Permission permission = permissionService.getPermission(id);
        if (permission == null) {            
            return new ResponseEntity<Permission>(HttpStatus.NOT_FOUND);
        }
  
        AuditDto audit = new AuditDto("Permission", "enablePermission", "Enable permission with id: "+id, "");
        auditTrail.sendMessage(request, audit);
        
        permission.setEnabled(false);
        permissionService.updatePermission(permission);
        
        return new ResponseEntity<Permission>(HttpStatus.NO_CONTENT);
    }
    */
    @RequestMapping(value = "/permission/validate", method = RequestMethod.POST)    
    public ResponseEntity<?> validatePermissions(@RequestParam("permissionKey") String permissionKey, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        Role role = user.getRole();        
        
        if (role.getRoleName().equals("ROLE_SYSADMIN") || role.getPermissions().contains(new Permission(permissionKey))) {
            return ResponseEntity.ok("Authorized");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }        
    }
}
