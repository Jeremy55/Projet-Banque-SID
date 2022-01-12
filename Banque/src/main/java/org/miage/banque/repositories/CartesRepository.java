package org.miage.banque.repositories;

import org.miage.banque.entities.carte.Carte;
import org.springframework.data.repository.CrudRepository;

public interface CartesRepository extends CrudRepository<Carte, Long> {
    public Carte findByNumero(String numero);
}
