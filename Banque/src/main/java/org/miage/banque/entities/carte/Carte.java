package org.miage.banque.entities.carte;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.miage.banque.entities.compte.Compte;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Carte {
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
    private boolean localisation;
    private double plafond;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "compte_id")
    @JsonBackReference
    private Compte compte;
}
