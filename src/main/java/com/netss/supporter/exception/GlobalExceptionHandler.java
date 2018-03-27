package com.netss.supporter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//TODO: user=fh message='improve exception handling'
@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<String> generalHandler() {
        String bodyOfResponse = "An Internal error has ocurred. Please contact support for more information.";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
