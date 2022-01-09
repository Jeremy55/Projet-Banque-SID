package org.miage.banque.entities.carte;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.miage.banque.entities.compte.Compte;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String numero;
    private String code;
    private String cryptogramme;

    private boolean active;
    private boolean contact;
    private boolean virtuelle;
    private boolean localisation;
    private double plafond;

    private Date expiration;

    @ManyToOne
    @JoinColumn(name = "compte_id")
    private Compte compte;
}
