package com.liferon.usermgt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RestException {

    public ResourceNotFoundException(Errors err) {
        super(err);
    }

    public ResourceNotFoundException(String error) {
        super(error);
    }

    
}
