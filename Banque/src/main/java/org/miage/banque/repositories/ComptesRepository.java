package org.miage.banque.repositories;

import org.miage.banque.entities.Compte;
import org.springframework.data.repository.CrudRepository;

public interface ComptesRepository extends CrudRepository<Compte, Long> {

}
