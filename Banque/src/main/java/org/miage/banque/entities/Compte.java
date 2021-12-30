package org.miage.banque.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @OneToOne(mappedBy = "compte")
    @JsonManagedReference
    private Client client;

    @OneToMany(mappedBy = "compte")
    @JsonManagedReference
    private Set<Carte> cartes;

    @OneToMany(mappedBy = "compte")
    @JsonManagedReference
    private Set<Operation> operations;

    private String IBAN;
    private double solde;
}
