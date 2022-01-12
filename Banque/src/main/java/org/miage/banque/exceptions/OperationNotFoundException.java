package org.miage.banque.exceptions;

public class OperationNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public OperationNotFoundException(String message) {
        super(message);
    }
}
