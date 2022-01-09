package org.miage.banque.entities.compte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteInput {
    @NotNull
    @DecimalMin(value = "0.0", message = "Le solde d'un nouveau compte doit être supérieur à 0")
    private double solde;
}
