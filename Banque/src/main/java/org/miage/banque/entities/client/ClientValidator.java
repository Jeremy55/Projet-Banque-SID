package org.miage.banque.entities.client;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

@Service
public class ClientValidator {

    private final Validator validator;

    ClientValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(ClientInput clientInput) {
        Set<ConstraintViolation<ClientInput>> violations = validator.validate(clientInput);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
