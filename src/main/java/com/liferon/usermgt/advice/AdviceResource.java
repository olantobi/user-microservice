package com.liferon.usermgt.advice;

import com.liferon.usermgt.exceptions.ConflictException;
import com.liferon.usermgt.exceptions.InternalServerErrorException;
import com.liferon.usermgt.exceptions.InvalidRequestParameterException;
import com.liferon.usermgt.type.RestErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceResource {    

    @ExceptionHandler(InvalidRequestParameterException.class)
    public ResponseEntity<RestErrorResponse> invalidRequestParameter(InvalidRequestParameterException ex) {
        RestErrorResponse model = new RestErrorResponse(ex.getError(),
                ex.getError_description());

        return new ResponseEntity<RestErrorResponse>(model, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestErrorResponse> accessDenied(AccessDeniedException ex) {
        RestErrorResponse model = new RestErrorResponse(ex.getMessage(),
                ex.getMessage());

        return new ResponseEntity<RestErrorResponse>(model, HttpStatus.FORBIDDEN);
    }        
    
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<RestErrorResponse> internalServerError(InternalServerErrorException ex) {
        RestErrorResponse model = new RestErrorResponse(ex.getError(),
                ex.getError_description());

        return new ResponseEntity<RestErrorResponse>(model, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<RestErrorResponse> conflictException(ConflictException ex) {
        RestErrorResponse model = new RestErrorResponse(ex.getError(),
                ex.getError_description());

        return new ResponseEntity<RestErrorResponse>(model, HttpStatus.CONFLICT);
    }
}
