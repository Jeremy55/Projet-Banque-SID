package org.miage.banque.entities.compte;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.client.Client;

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

    @Column(unique = true)
    private String IBAN;
    private double solde;
}
