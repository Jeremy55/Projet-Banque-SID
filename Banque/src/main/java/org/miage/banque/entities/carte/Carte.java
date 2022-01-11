package org.miage.banque.entities.carte;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

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

    @OneToMany(mappedBy = "carte",cascade = CascadeType.ALL)
    private Set<Operation> operations;

    public boolean isLimitReached(double toAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        double total = 0;

        if(getOperations() != null) {
            for (Operation op : getOperations()) {
                if (op.getDate().after(calendar.getTime())) {
                    total += op.getMontant();
                }
            }
        }

        return  (total + toAdd) >= getPlafond();
    }
}
