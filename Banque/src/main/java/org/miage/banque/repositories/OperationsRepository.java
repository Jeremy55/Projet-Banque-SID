package org.miage.banque.repositories;

import org.miage.banque.entities.Role;
import org.miage.banque.entities.operation.Operation;
import org.springframework.data.repository.CrudRepository;

public interface OperationsRepository extends CrudRepository<Operation, Long> {
}
