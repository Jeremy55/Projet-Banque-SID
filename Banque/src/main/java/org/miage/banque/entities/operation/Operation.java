package org.miage.banque.entities.operation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.compte.Compte;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String libelle;
    private double montant;
    private double montant_avant_conversion;
    private String devise;
    private String categorie;
    private String pays;
    private Date date;

    private String IBAN_debiteur;

    private boolean contact;
    private boolean online;

    @ManyToOne
    @JoinColumn(name = "carte_id")
    private Carte carte;
}
