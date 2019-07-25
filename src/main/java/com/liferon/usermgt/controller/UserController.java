/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import com.liferon.usermgt.exceptions.ConflictException;
import com.liferon.usermgt.exceptions.InvalidRequestParameterException;
import com.liferon.usermgt.dto.AuditDto;
import com.liferon.usermgt.model.Role;
import com.liferon.usermgt.model.Tenant;
import com.liferon.usermgt.model.User;
import com.liferon.usermgt.dto.UserDto;
import com.liferon.usermgt.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.liferon.usermgt.event.AuditTrailEvent;
import com.liferon.usermgt.service.RoleService;
import com.liferon.usermgt.service.TenantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author olanrewaju.ebenezer
 */
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService; 
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
      
    @PreAuthorize("hasPermission(#user, 'can_view_users') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers(HttpServletRequest request) {
        List<User> users = userService.getAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }    
      
    @PreAuthorize("hasPermission(#user, 'can_view_user') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<User> getUser(@PathVariable("id") String id, HttpServletRequest request) {        
        User user = userService.findById(id);
        if (user == null) {            
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
                
    @PreAuthorize("hasPermission(#user, 'can_create_user') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@Valid @ModelAttribute UserDto userDto, BindingResult result, UriComponentsBuilder ucBuilder, HttpServletRequest request) throws InvalidRequestParameterException, ConflictException {

        if (userDto.getRoleId() == null) {            
            throw new InvalidRequestParameterException("Bad Request", "RoleId cannot be empty");
        }
        
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleService.getRole(userDto.getRoleId());
        Tenant tenant = null;
        if (userDto.getTenantId() != null)
            tenant = tenantService.getTenant(userDto.getTenantId());
                
        if (role == null) {            
            throw new InvalidRequestParameterException("Bad Request", "Invalid role id specified");
        }                
        
        User user = modelMapper.map(userDto, User.class);
        if (tenant != null)
            user.setTenant(tenant);
        user.setRole(role);
        user.setCreatedIp(request.getRemoteAddr());
        
        if (userService.isUserExist(user)) {            
            throw new ConflictException("Conflict", "User with username "+user.getUsername()+" already exists");
        }        
        
        User createdUser = userService.save(user);
  
        AuditDto audit = new AuditDto("User", "createUser", "CREATE", "", createdUser.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(createdUser.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
      
    @PreAuthorize("hasPermission(#user, 'can_update_user') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @Valid @ModelAttribute UserDto userDto, 
            BindingResult result, HttpServletRequest request) throws InvalidRequestParameterException, NotFoundException { 
                
        if (result.hasFieldErrors()) {
            String errors = result.getFieldErrors().stream()
                .map(p -> p.getDefaultMessage()).collect(Collectors.joining("\n"));            
            throw new InvalidRequestParameterException("Bad Request", errors);
        }
        
        User currentUser = userService.findById(id);
          
        if (currentUser==null) {            
            throw new NotFoundException("User with id "+id +" does not exist");
        }

        currentUser.setFirstName(userDto.getFirstName());
        currentUser.setLastName(userDto.getLastName());
        currentUser.setMobileNumber(userDto.getMobileNumber());
        currentUser.setEmail(userDto.getEmail());                          
        
        User newUser = userService.update(currentUser);
        
        AuditDto audit = new AuditDto("User", "updateUser", "UPDATE", currentUser.toString(), newUser.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<User>(newUser, HttpStatus.OK);
    }
      /*
    @PreAuthorize("hasPermission(#user, 'can_delete_user') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> disableUser(@PathVariable("id") String id, HttpServletRequest request) {          
        User user = userService.findById(id);
        if (user == null) {            
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        
        user.setEnabled(false);
        userService.update(user);
        return new ResponseEntity<User>(HttpStatus.OK);
    }
    
    @PreAuthorize("hasPermission(#user, 'can_update_user') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user/{id}/enable", method = RequestMethod.POST)
    public ResponseEntity<User> enableUser(@PathVariable("id") String id, HttpServletRequest request) {        
        User user = userService.findById(id);
        if (user == null) {            
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        
        AuditDto audit = new AuditDto("User", "enableUser", "Enable user with id: "+id, "");
        auditTrail.sendMessage(request, audit);
        
        user.setEnabled(true);
        userService.update(user);
        return new ResponseEntity<User>(HttpStatus.OK);
    }            
    
    /*
    @PreAuthorize("hasPermission(#user, 'can_delete_users') or hasRole('ROLE_SYSADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);
  
        User user = userService.findById(id);
        if (user == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
  
        userService.deleteUser(user);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
*/
}
