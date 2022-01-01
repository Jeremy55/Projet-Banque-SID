package org.miage.banque.entities.compte;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.miage.banque.entities.client.ClientInput;
import org.springframework.stereotype.Service;

@Service
public class CompteValidator {

    private final Validator validator;

    CompteValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(CompteInput compteInput) {
        Set<ConstraintViolation<CompteInput>> violations = validator.validate(compteInput);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
