package org.miage.banque.repositories;

import org.miage.banque.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientsRepository extends CrudRepository<Client, Long> {
}
