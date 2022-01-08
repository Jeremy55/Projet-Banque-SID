package org.miage.banque.entities.client;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.compte.Compte;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;


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

    @Column(unique = true)
    private String no_passeport;
    @Column(unique = true)
    private String telephone;
    @Column(unique = true)
    private String email;

    private String mot_de_passe;

    //TODO LINK ACCOUNT TO CLIENT.
    @OneToOne
    private Compte compte;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "client_roles",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id",referencedColumnName = "id"))
    private Collection<Role> roles = new ArrayList<>();
}

