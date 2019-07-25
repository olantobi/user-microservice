/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.controller;

import com.liferon.usermgt.dto.ClientDto;
import com.liferon.usermgt.model.Client;
import com.liferon.usermgt.service.ClientService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.liferon.usermgt.component.AuditTrailHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author olanrewaju.ebenezer
 */
@ApiIgnore
@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuditTrailHandler auditTrail;
    
    @ApiIgnore
    @PostMapping("/client")
    @PreAuthorize("hasPermission(#client, 'can_create_client')")
    public ResponseEntity<?> addClient(@Valid @ModelAttribute ClientDto clientDto, BindingResult result, 
            UriComponentsBuilder ucBuilder, HttpServletRequest request) {
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        
        Client client = modelMapper.map(clientDto, Client.class);
        
        Client createdClient = clientService.saveClient(client);        
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/client/{id}").buildAndExpand(createdClient.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
