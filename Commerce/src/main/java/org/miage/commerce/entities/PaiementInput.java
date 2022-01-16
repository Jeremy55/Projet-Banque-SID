package org.miage.commerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementInput{

    @NotNull
    @Size(min = 1, max = 100)
    private String libelle;
    @NotNull
    @DecimalMin(value = "0.01")
    private double montant;
    @NotNull
    @Size(min = 1, max = 100)
    private String categorie;

    @NotNull
    @Size(min = 1, max = 100)
    private String pays;
    @NotNull
    @Size(min = 1, max = 100)
    private String IBAN_debiteur;

    @NotNull
    @Size(min = 1, max = 6)
    private String devise;

    @NotNull
    private boolean contact;
    @NotNull
    private boolean online;

    private String longitude;
    private String latitude;

    @NotNull
    @Size(min = 16, max = 16)
    private  String numero;
    @Size(min =4,max =4)
    private String code;
    @Size(min =3,max =3)
    private String cryptogramme;
}