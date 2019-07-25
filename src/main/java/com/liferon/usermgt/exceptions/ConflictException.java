/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.exceptions;

import org.springframework.validation.Errors;

/**
 *
 * @author olanrewaju.ebenezer
 */

public class ConflictException extends RestException {
    
    public ConflictException(Errors err) {
        super(err);
    }

    public ConflictException(String errorMessage) {
        super(errorMessage);
    }

    public ConflictException(String error, String description) {
        super(error, description);
    }        
}
