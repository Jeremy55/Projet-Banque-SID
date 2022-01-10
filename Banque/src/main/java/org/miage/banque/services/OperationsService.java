package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.repositories.OperationsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OperationsService {
    private final OperationsRepository operationsRepository;

    public Operation create(Operation operation) {
        log.info("Création d'une opération {} pour un montant de {}", operation.getCategorie(), operation.getMontant());
        return operationsRepository.save(operation);
    }
}
