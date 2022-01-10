package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.delegate.ConversionServiceDelegate;
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
    private final ConversionServiceDelegate conversionServiceDelegate;

    public Operation create(Operation operation) {
        log.info("Création d'une opération {} pour un montant de {}", operation.getCategorie(), operation.getMontant());
        log.info("Vérification du montant disponible sur le compte : {}", operation.getCompte().getSolde());

        double convertedOperation = conversionServiceDelegate.callStudentServiceAndGetData("USD", "EUR", operation.getMontant());
        operation.setMontant_apres_conversion(convertedOperation);

        if (operation.getMontant() >= operation.getCompte().getSolde()) {
            log.error("Le montant {} est supérieur au montant disponible sur le compte {}", operation.getMontant(), operation.getCompte().getIBAN());
            throw new RuntimeException("Le montant demandé est supérieur au solde du compte");
        }
        return operationsRepository.save(operation);
    }
}
