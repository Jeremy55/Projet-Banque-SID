package org.miage.banque.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String numero;
    private String code;
    private String cryptogramme;

    private boolean active;
    private boolean contact;
    private boolean virtuelle;
    private String localisation;
    private double plafond;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_id")
    @JsonBackReference
    private Compte compte;
}
