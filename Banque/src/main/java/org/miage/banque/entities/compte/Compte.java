package org.miage.banque.entities.compte;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.client.Client;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
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

    @OneToMany(mappedBy = "compte",cascade = CascadeType.ALL)
    private Collection<Carte> cartes = new ArrayList<Carte>();

    @OneToMany(mappedBy = "compte",cascade = CascadeType.ALL)
    private Set<Operation> operations;

    @OneToOne(mappedBy = "compte")
    private Client client;

    @Column(unique = true)
    private String IBAN;
    private double solde;

}
