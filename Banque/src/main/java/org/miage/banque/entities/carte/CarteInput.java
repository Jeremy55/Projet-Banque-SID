package org.miage.banque.entities.carte;

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
public class CarteInput {

    @NotBlank
    @NotNull
    @Size(min = 16, max = 16)
    private String numero;

    @NotBlank
    @NotNull
    @Size(min = 4, max = 4)
    private String code;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 3)
    private String cryptogramme;

    @NotNull
    private boolean active;
    @NotNull
    private boolean contact;
    @NotNull
    private boolean virtuelle;
    @NotNull
    private boolean localisation;

    @NotNull
    @DecimalMin(value= "0.0")
    private double plafond;

    @NotNull
    private Long compte_id;


}
