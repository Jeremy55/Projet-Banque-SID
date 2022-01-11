package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.repositories.ComptesRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComptesService {

    private final ComptesRepository comptesRepository;

    public Compte getCompte(Long id) {
        return comptesRepository.findById(id).orElseThrow(() -> new CompteNotFoundException("Ce compte n'existe pas."));
    }

    public Iterable<? extends Compte> getAllComptes() {
        return comptesRepository.findAll();
    }

    public void credit(String IBAN, double montant) {
        Compte compte = comptesRepository.findCompteByIBAN(IBAN);

        if(compte == null) {
            log.warn("Compte non trouvé pour l'IBAN {} communication de l'opération à la banque du propriétaire.", IBAN);
            return;
        }

        compte.setSolde(compte.getSolde() + montant);
        comptesRepository.save(compte);
    }

    public void debit(String IBAN, double montant) {
        Compte compte = comptesRepository.findCompteByIBAN(IBAN);
        compte.setSolde(compte.getSolde() - montant);
        comptesRepository.save(compte);
    }

}
