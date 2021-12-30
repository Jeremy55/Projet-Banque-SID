package org.miage.banque.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private double taux_conversion;
    private String devise;
    private double montant_converti;
    private String categorie;
    private String pays;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Compte compte;
}
