package org.miage.banque.entities.compte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteInput {

    @NotBlank
    @NotNull
    @Size(min = 16, max = 34)
    private String IBAN;

    @NotBlank
    @NotNull
    private double solde;
}
