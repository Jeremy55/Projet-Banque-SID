package org.miage.banque.entities.carte;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

@Service
public class CarteValidator {

    private final Validator validator;

    CarteValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(CarteInput carteInput){
        Set<ConstraintViolation<CarteInput>> violations = validator.validate(carteInput);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }

}
