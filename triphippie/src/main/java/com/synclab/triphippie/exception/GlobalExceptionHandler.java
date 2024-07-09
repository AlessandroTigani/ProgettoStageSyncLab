package com.synclab.triphippie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UniqueFieldException.class)
    public ResponseEntity<Object> handleUniqueFieldException(UniqueFieldException ex, WebRequest request) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setTimestamp(new Date());
        customErrorResponse.setStatus(HttpStatus.CONFLICT.value());
        customErrorResponse.setError("Conflict");
        customErrorResponse.setMessage(ex.getMessage());
        customErrorResponse.setPath(request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(customErrorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setTimestamp(new Date());
        customErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        customErrorResponse.setError("Bad Request");
        customErrorResponse.setMessage(ex.getMessage());
        customErrorResponse.setPath(request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customErrorResponse);
    }
}
