package org.miage.banque.entities;

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
    private boolean sansContact;
    private boolean virtuelle;
    private String localisation;
    private double plafond;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false, updatable = false, insertable = false)
    private Client client;
}
