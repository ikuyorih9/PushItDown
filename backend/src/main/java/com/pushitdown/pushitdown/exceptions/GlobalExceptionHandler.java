package com.pushitdown.pushitdown.exceptions;

import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient.ResponseSpec;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserRegisteredException.class)
    public ResponseEntity<String> handleUserRegisteredException(UserRegisteredException ex) {
        System.out.println("[USER REGISTERED EXCEPTION]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex){
        System.out.println("[NOT FOUND EXCEPTION]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    @ExceptionHandler(BadFormsException.class)
    public ResponseEntity<String> handleBadFormsException(BadFormsException ex){
        System.out.println("[BAD FORMS EXCEPTION]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handelDataIntegrityViolationException(DataIntegrityViolationException ex){
        System.out.println("[DATA INTEGRITY VIOLATION EXCEPTION]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedsException(UnauthorizedException ex){
        System.out.println("[UNAUTHORIZED EXCEPTION]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }
    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<String> handelConstraintViolationException(org.hibernate.exception.ConstraintViolationException ex){
        System.out.println("[CONSTRAINT VIOLATION EXCEPTION]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
    //     System.out.println("[RUNTIME EXCEPTION]: " + ex.getMessage() + ex.getStackTrace());
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    // }
}
