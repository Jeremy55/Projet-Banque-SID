package org.miage.banque.controllers;

import org.miage.banque.exceptions.CompteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = CompteNotFoundException.class)
    public ResponseEntity<Object> exception(CompteNotFoundException exception) {
        return new ResponseEntity<>("Ce compte n'existe pas !", HttpStatus.NOT_FOUND);
    }
}
