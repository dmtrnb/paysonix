package com.example.paysonix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleByDefault(Exception exception) {
        return ResponseEntity
                .internalServerError()
                .body(exception.getMessage());
    }
}
