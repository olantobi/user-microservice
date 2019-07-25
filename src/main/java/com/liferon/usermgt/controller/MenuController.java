/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.liferon.usermgt.exceptions.ConflictException;
import com.liferon.usermgt.exceptions.InvalidRequestParameterException;
import javassist.NotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.liferon.usermgt.dto.AuditDto;
import com.liferon.usermgt.dto.MenuDto;
import com.liferon.usermgt.event.AuditTrailEvent;
import com.liferon.usermgt.model.Menu;
import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.service.MenuService;
import com.liferon.usermgt.service.PermissionService;
import org.modelmapper.ModelMapper;
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
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PermissionService permissionService;
    
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#menu, 'can_view_menus') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<List<Menu>> listAllMenus(@RequestParam(name="onlyParents", required = false) boolean onlyParents, HttpServletRequest request) {
        List<Menu> menus = null;
        
        if (onlyParents)
            menus = menuService.getParentMenus();
        else
            menus = menuService.getAllEnabled();      
        
        if(menus.isEmpty()){
            return new ResponseEntity<List<Menu>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Menu>>(menus, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/menu/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#menu, 'can_view_menu') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Menu> getMenu(@PathVariable("id") long id) {
        Menu menu = menuService.getMenu(id);
        if (menu == null) {
            return new ResponseEntity<Menu>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Menu>(menu, HttpStatus.OK);
    }
        
    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#menu, 'can_create_menu') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Void> createMenu(@Valid @ModelAttribute MenuDto menuDto, BindingResult result, UriComponentsBuilder ucBuilder, 
            HttpServletRequest request) throws InvalidRequestParameterException, ConflictException, InvalidRequestParameterException {
        
        if (result.hasFieldErrors()) {
            String errors = result.getFieldErrors().stream()
                .map(p -> p.getDefaultMessage()).collect(Collectors.joining("\n"));            
            throw new InvalidRequestParameterException("Bad Request", errors);
        }
        
        if (menuService.menuNameExists(menuDto.getMenuName())) {            
            throw new ConflictException("Menu Conflict", "");
        }
                        
        Permission permission = permissionService.getPermissionByKey(menuDto.getPermissionKey());
        if (permission == null)
            throw new InvalidRequestParameterException("Bad Request", "Permission key "+menuDto.getPermissionKey()+" does not exist");
                        
        Menu menu = modelMapper.map(menuDto, Menu.class);                            
        menu.setPermission(permission);        
        Menu createdMenu = menuService.saveMenu(menu);
                
        if (menuDto.getParentMenuId() > 0) {
            Menu parentMenu = menuService.getMenu(menuDto.getParentMenuId());
            if (parentMenu == null)
                throw new InvalidRequestParameterException("Bad Request", "Parent menu "+menuDto.getParentMenuId()+" does not exist");  
            
            parentMenu.addChildMenu(createdMenu);
            menuService.saveMenu(parentMenu);
        }                
        
        AuditDto audit = new AuditDto("Menu", "createMenu", "CREATE", "", createdMenu.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/menu/{id}").buildAndExpand(createdMenu.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }      
      
    @RequestMapping(value = "/menu/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#menu, 'can_update_menu') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Menu> updateMenu(@PathVariable("id") long id, @Valid @ModelAttribute MenuDto menuDto, BindingResult result, 
            HttpServletRequest request) throws InvalidRequestParameterException, NotFoundException { 
        
        Menu currentMenu = menuService.getMenu(id);
          
        if (currentMenu == null) {                        
            throw new NotFoundException("Menu with id "+id +" does not exist");
        }
        
        Permission permission = permissionService.getPermissionByKey(menuDto.getPermissionKey());
        if (permission == null)
            throw new InvalidRequestParameterException("Bad Request", "Permission key "+menuDto.getPermissionKey()+" does not exist");                       
        
        currentMenu.setMenuName(menuDto.getMenuName());
        currentMenu.setMenuUrl(menuDto.getMenuUrl());
        currentMenu.setPermission(permission);
        currentMenu.setEnabled(menuDto.isEnabled());                
          
        Menu newMenu = menuService.saveMenu(currentMenu);
        
        if (menuDto.getParentMenuId() > 0) {
            Menu parentMenu = menuService.getMenu(menuDto.getParentMenuId());
            if (parentMenu == null)
                throw new InvalidRequestParameterException("Bad Request", "Parent menu "+menuDto.getParentMenuId()+" does not exist");  
            
            parentMenu.addChildMenu(newMenu);
            menuService.saveMenu(parentMenu);
        }
        
        AuditDto audit = new AuditDto("Menu", "updateMenu", "UPDATE", currentMenu.toString(), newMenu.toString());
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Menu>(newMenu, HttpStatus.OK);
    }
      /*
    @RequestMapping(value = "/menu/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Menu> deleteMenu(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Menu with id " + id);
  
        Menu menu = menuService.getMenu(id);
        if (menu == null) {
            System.out.println("Unable to delete. Menu with id " + id + " not found");
            return new ResponseEntity<Menu>(HttpStatus.NOT_FOUND);
        }
  
        menuService.deleteMenu(menu);
        return new ResponseEntity<Menu>(HttpStatus.NO_CONTENT);
    }
    */
    
    @RequestMapping(value = "/menu/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#menu, 'can_delete_menu') or hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Menu> deleteMenu(@PathVariable("id") long id, HttpServletRequest request) {        
  
        Menu menu = menuService.getMenu(id);
        if (menu == null) {            
            return new ResponseEntity<Menu>(HttpStatus.NOT_FOUND);
        }
          
        menuService.deleteMenu(menu);
        
        AuditDto audit = new AuditDto("Menu", "deleteMenu", "DELETE", menu.toString(), "");
        eventPublisher.publishEvent(new AuditTrailEvent(audit, request));
        
        return new ResponseEntity<Menu>(HttpStatus.OK);
    }
}
