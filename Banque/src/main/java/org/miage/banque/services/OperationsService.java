package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.delegate.ConversionServiceDelegate;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.repositories.OperationsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OperationsService {
    private final OperationsRepository operationsRepository;
    private final ComptesService comptesService;
    private final ConversionServiceDelegate conversionServiceDelegate;

    public Operation create(Operation operation) {
        log.info("Création d'une opération {} pour un montant de {}", operation.getCategorie(), operation.getMontant());

        if(!Objects.equals(operation.getDevise(), operation.getCarte().getCompte().getDevise())) {
            log.info("Conversion de la devise de l'opération {} vers la devise du compte {}.", operation.getDevise(), operation.getCarte().getCompte().getDevise());
            double convertedOperation = conversionServiceDelegate.callStudentServiceAndGetData(operation.getDevise(), operation.getCarte().getCompte().getDevise(), operation.getMontant());
            operation.setMontant_avant_conversion(operation.getMontant());
            operation.setMontant(convertedOperation);
        }

        log.info("Vérification du montant disponible sur le compte : {}", operation.getCarte().getCompte().getSolde());
        if (operation.getMontant() >= operation.getCarte().getCompte().getSolde()) {
            log.error("Le montant {} est supérieur au montant disponible sur le compte {}", operation.getMontant(), operation.getCarte().getCompte().getIBAN());
            throw new RuntimeException("Le montant demandé est supérieur au solde du compte");
        }

        //TODO : Vérifier expiration de la carte.
        //TODO : Vérifier plafond de la carte.
        //TODO : Géolocalisation.

        log.info("Mise à jour du solde du compte avec un débit de {}", operation.getMontant());
        comptesService.debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());

        log.info("Crédit du montant de {} sur le compte {}", operation.getMontant(), operation.getIBAN_debiteur());
        comptesService.credit(operation.getIBAN_debiteur(), operation.getMontant());

        return operationsRepository.save(operation);
    }
}
