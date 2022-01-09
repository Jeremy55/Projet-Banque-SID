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

    @NotNull
    private boolean active;
    @NotNull
    private boolean contact;
    @NotNull
    private boolean virtuelle;
    @NotNull
    private boolean localisation;

    @NotNull
    @DecimalMin(value= "100")
    private double plafond;
}
