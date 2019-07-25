/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author olanrewaju.ebenezer
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class CustomAuthException extends AuthenticationException {        
       
    public CustomAuthException(String errorMessage) {
        super(errorMessage);
    }
}
