package org.miage.banque.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String nom;
    private String prenom;
    private String pays;
    private String no_passeport;
    private String telephone;
    private String secret;
    @OneToMany(mappedBy = "client")
    private Set<Card> cards;
    @OneToOne(mappedBy = "client")
    private Compte compte;
}

