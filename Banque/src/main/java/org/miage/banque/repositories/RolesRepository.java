package org.miage.banque.repositories;

import org.miage.banque.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RolesRepository extends CrudRepository<Role, Long> {
    Role findByNom(String nom);
}
