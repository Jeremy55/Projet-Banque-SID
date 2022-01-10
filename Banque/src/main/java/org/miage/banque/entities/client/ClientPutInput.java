package org.miage.banque.entities.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPutInput {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String nom;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String prenom;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String pays;

    @NotBlank
    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    private String telephone;

    @NotBlank
    @NotNull
    private String mot_de_passe;

}
