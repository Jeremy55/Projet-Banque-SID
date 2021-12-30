package org.miage.banque.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private double montant_apres_conversion;
    private double taux_conversion;
    private String devise;
    private String categorie;
    private String pays;
    private Timestamp date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_id")
    @JsonBackReference
    private Compte compte;
}
