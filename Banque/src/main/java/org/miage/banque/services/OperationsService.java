package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.delegate.ConversionServiceDelegate;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.exceptions.OperationNotFoundException;
import org.miage.banque.repositories.OperationsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OperationsService {
    private final OperationsRepository operationsRepository;
    private final ComptesService comptesService;
    private final ConversionServiceDelegate conversionServiceDelegate;
    private final CartesService cartesService;

    public Operation create(Operation operation) {
        log.info("Création d'une opération {} pour un montant de {}", operation.getCategorie(), operation.getMontant());

        Calendar calendar = Calendar.getInstance();
        operation.setDate(calendar.getTime());

        if(!operation.getCarte().isActive()){
            log.error("La carte n'est pas active");
            throw new RuntimeException("La carte n'est pas active");
        }

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

        if(operation.getDate().after(operation.getCarte().getExpiration())){
            log.error("La date de l'opération est supérieur à la date d'expiration de la carte");
            throw new RuntimeException("La date de l'opération est supérieur à la date d'expiration de la carte");
        }

        if(operation.getCarte().isLimitReached(operation.getMontant())){
            log.error("La carte {} a atteint son plafond pour le mois glissant en cours.", operation.getCarte().getNumero());
            throw new RuntimeException("La carte a atteint son plafond pour le mois glissant en cours.");
        }
        //TODO : Géolocalisation.

        log.info("Mise à jour du solde du compte avec un débit de {}", operation.getMontant());
        comptesService.debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());

        log.info("Crédit du montant de {} sur le compte {}", operation.getMontant(), operation.getIBAN_debiteur());
        comptesService.credit(operation.getIBAN_debiteur(), operation.getMontant());

        if(operation.getCarte().isVirtuelle()){
            log.info("La carte est virtuelle donc désactivation de la carte {}", operation.getCarte().getNumero());
            cartesService.deleteCarte(operation.getCarte());
        }

        return operationsRepository.save(operation);
    }

    public Iterable<Operation> getAll() {
        return operationsRepository.findAll();
    }

    public Operation getOperation(Long id) {
        return operationsRepository.findById(id).orElseThrow( () -> new OperationNotFoundException("Cette opération n'exsite pas."));
    }
}
