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
import com.liferon.usermgt.dto.RoleDto;
import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.model.Tenant;
import com.liferon.usermgt.model.User;
import com.liferon.usermgt.service.PermissionService;
import com.liferon.usermgt.service.RoleService;
import com.liferon.usermgt.service.TenantService;
import com.liferon.usermgt.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#role, 'can_view_roles') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<List<Role>> listAllRoles(HttpServletRequest request) {
        List<Role> roles = roleService.getAllEnabled();
        
        if(roles.isEmpty()){
            return new ResponseEntity<List<Role>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
    }
      
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#role, 'can_view_role') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Role> getRole(@PathVariable("id") String id) {
        Role role = roleService.getRole(id);
        if (role == null) {
            return new ResponseEntity<Role>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Role>(role, HttpStatus.OK);
    }
        
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#role, 'can_create_role') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Void> createRole(@RequestParam("roleName") String roleName, @RequestParam("tenantId") Optional<String> tenantId, 
            UriComponentsBuilder ucBuilder, Authentication authentication, HttpServletRequest request) throws InvalidRequestParameterException, ConflictException {
                
        if (roleName == null || roleName.isEmpty()) {
            throw new InvalidRequestParameterException("Bad Request", "Role name cannot be empty");            
        } 
        
        else if (!roleName.startsWith("ROLE_")) {
            throw new InvalidRequestParameterException("Bad Request", "Role name must start with ROLE_");
        }
        
        if (roleService.roleNameExists(roleName)) {            
            throw new ConflictException("Conflict", "Role with name "+roleName+" already exists");
        }                
        
        Role role = new Role(roleName);
        role.setEnabled(true);                        
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSADMIN")) && tenantId.isPresent()) {
            Tenant tenant = tenantService.getTenant(tenantId.get());
            if (tenant == null)
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);               
            role.setTenant(tenant);
        } else if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSADMIN"))) {            
            User user = userService.findByUsername(authentication.getName());
            role.setTenant(user.getTenant());
        }
        
        Role createdRole = roleService.saveRole(role);
        
        AuditDto audit = new AuditDto("Role", "createRole", "CREATE", "", createdRole.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/role/{id}").buildAndExpand(createdRole.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }      
      
    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#role, 'can_update_role') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Role> updateRole(@PathVariable("id") String id, @Valid @ModelAttribute RoleDto roleDto, BindingResult result, 
            HttpServletRequest request) throws InvalidRequestParameterException, NotFoundException, InvalidRequestParameterException {
        
        if (result.hasFieldErrors()) {
            String errors = result.getFieldErrors().stream()
                .map(p -> p.getDefaultMessage()).collect(Collectors.joining("\n"));            
            throw new InvalidRequestParameterException("Bad Request", errors);
        }
        
        Role currentRole = roleService.getRole(id);
          
        if (currentRole==null) {            
            throw new NotFoundException("Role with id "+id+" does not exist");
        }
        
        if (roleDto.getRoleName() == null || roleDto.getRoleName().isEmpty()) {
            throw new InvalidRequestParameterException("Bad Request", "Role name cannot be empty");
        } 
        
        else if (!roleDto.getRoleName().startsWith("ROLE_")) {
            throw new InvalidRequestParameterException("Bad Request", "Role name must start with ROLE_");
        }
        
        currentRole.setRoleName(roleDto.getRoleName());
        currentRole.setEnabled(roleDto.isEnabled());            
          
        Role newRole = roleService.saveRole(currentRole);
        
        AuditDto audit = new AuditDto("Role", "updateRole", "UPDATE", currentRole.toString(), newRole.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Role>(newRole, HttpStatus.OK);
    }
      /*
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Role> deleteRole(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Role with id " + id);
  
        Role role = roleService.getRole(id);
        if (role == null) {
            System.out.println("Unable to delete. Role with id " + id + " not found");
            return new ResponseEntity<Role>(HttpStatus.NOT_FOUND);
        }
  
        roleService.deleteRole(role);
        return new ResponseEntity<Role>(HttpStatus.NO_CONTENT);
    }
    *
    
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#role, 'can_delete_role') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Role> deleteRole(@PathVariable("id") String id) {
        //System.out.println("Fetching & disabling Role with id " + id);
  
        Role role = roleService.getRole(id);
        if (role == null) {
            System.out.println("Unable to delete. Role with id " + id + " not found");
            return new ResponseEntity<Role>(HttpStatus.NOT_FOUND);
        }
  
        role.setEnabled(false);
        roleService.updateRole(role);
        
        AuditDto audit = new AuditDto("Role", "deleteRole", "DELETE", role.toString(), "");
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
    
        return new ResponseEntity<Role>(HttpStatus.ACCEPTED);
    }
    /*
    @RequestMapping(value = "/role/{id}/enable", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#role, 'can_update_role') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Role> enableRole(@PathVariable("id") String id) {
        //System.out.println("Fetching & enabling Role with id " + id);
  
        Role role = roleService.getRole(id);
        if (role == null) {
            //System.out.println("Unable to delete. Role with id " + id + " not found");
            return new ResponseEntity<Role>(HttpStatus.NOT_FOUND);
        }
  
        role.setEnabled(true);
        roleService.updateRole(role);
        
        return new ResponseEntity<Role>(HttpStatus.ACCEPTED);
    }
    */
    @RequestMapping(value = "/role/{id}/permission", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#role, 'can_assign_permission') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Role> addPermission(@PathVariable("id") String id, @RequestParam("permissionKey") String permissionKey, 
            @RequestParam("description") String description, HttpServletRequest request) throws NotFoundException, InvalidRequestParameterException {
  
        Permission permission = permissionService.getPermissionByKey(permissionKey);
        if (permission == null)
            permission = permissionService.savePermission(new Permission(permissionKey, description));        
        
        Role role = roleService.getRole(id);
        if (role == null) {               
            throw new NotFoundException("Role with id "+id+" does not exist");          
        }
        
        if (role.getPermissions().contains(permission)) {
            throw new InvalidRequestParameterException(role.getRoleName() +" already has "+permissionKey + " permission");
        }
        
        role.addPermission(permission);
        
        roleService.updateRole(role);
        
        AuditDto audit = new AuditDto("Role", "addPermission", "ASSIGN", "", permissionKey);
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Role>(HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value = "/role/{id}/permissions", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#user, 'can_view_permissions') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<List<Permission>> viewPermissions(@PathVariable("id") String id) {
        Role role = roleService.getRole(id);
        if (role == null) {           
            return new ResponseEntity<List<Permission>>(HttpStatus.NOT_FOUND);            
        }
        
        List<Permission> permissions = role.getPermissions();
        
        return new ResponseEntity<List<Permission>>(permissions, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/role/{id}/permissions", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#user, 'can_assign_permissions') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<?> addPermissions(@PathVariable("id") String id, @Valid @RequestBody List<PermissionDto> permissions, 
            BindingResult bindingResult, HttpServletRequest request) {
  
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<Role>(HttpStatus.BAD_REQUEST);
        }
        
        Role role = roleService.getRole(id);
        if (role == null) {
            //System.out.println("Unable to add permission. Role with id " + id + " not found");            
            return new ResponseEntity<Role>(HttpStatus.NOT_FOUND);            
        }
        
        String message = "";
        int addedCounter = 0;
        int exemptCounter = 0;
        
        for (PermissionDto dto : permissions) {
            Permission permission = permissionService.getPermissionByKey(dto.getPermissionKey());
            if (permission == null) {
                permission = permissionService.savePermission(new Permission(dto));
            }
            
            if (role.getPermissions().contains(permission)) {
                exemptCounter++;
                message += permission.getPermissionKey()+", ";
            } else {
                addedCounter++;
                role.addPermission(permission);
            }                                    
        }        
        
        roleService.updateRole(role);
        
        AuditDto audit = new AuditDto("Role", "addPermissions", "ASSIGN", "", "Multiple permissions e.g. "+permissions.get(0).getPermissionKey());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        if (exemptCounter == 0)
            return ResponseEntity.ok(addedCounter +" permissions added. ");
        else
            return ResponseEntity.ok(addedCounter +" permissions added. "+exemptCounter +" permissions already existed: "+message);
    }
}
