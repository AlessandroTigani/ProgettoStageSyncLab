package com.synclab.triphippie.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle errors caused by the violation of unique constraints.
     */
    @ExceptionHandler(UniqueFieldException.class)
    public ResponseEntity<String> handleUniqueFieldException(UniqueFieldException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


    /**
     * Handle issues related to the validation process of data within a DTO.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle the error that occurs when a method argument is of the wrong type.
     * This exception is thrown when the argument provided in a request cannot be converted to the required type.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * Notify the absence of the desired element.
     */
    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<String> handleEntryNotFoundException(EntryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    /**
     * Notify the absence of the Authorization Header
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * Triggered if the authorization token is expired
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<Object> handleUserUnauthorizedException() {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
