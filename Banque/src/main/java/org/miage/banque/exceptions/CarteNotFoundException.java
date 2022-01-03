package org.miage.banque.exceptions;

public class CarteNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public CarteNotFoundException(String message) {
        super(message);
    }
}
