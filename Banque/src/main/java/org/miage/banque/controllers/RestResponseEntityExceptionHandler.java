package org.miage.banque.controllers;

import org.miage.banque.exceptions.CarteNotFoundException;
import org.miage.banque.exceptions.ClientNotFoundException;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.exceptions.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            CompteNotFoundException.class,
            ClientNotFoundException.class,
            CarteNotFoundException.class
            })
    public ResponseEntity<Object> exception(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value ={
            InvalidTokenException.class
    })
    public ResponseEntity<Object> exception(InvalidTokenException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
