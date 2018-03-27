package com.netss.supporter.exception;

import com.netss.supporter.exception.helper.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;

//TODO: user=fh message='improve exception handling'
@ControllerAdvice
public class GlobalExceptionHandler {

    private final String GENERAL_ERROR_MESSAGE = "An Internal error has occurred. Please contact support for more information.";

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> generalHandler(Exception ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_ERROR_MESSAGE));
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<Object> databaseError(SQLException sqlEx, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_ERROR_MESSAGE));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityConstraintViolation(
        DataIntegrityViolationException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, GENERAL_ERROR_MESSAGE));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
