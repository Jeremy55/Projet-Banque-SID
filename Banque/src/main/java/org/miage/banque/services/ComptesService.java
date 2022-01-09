package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.compte.CompteInput;
import org.miage.banque.entities.compte.CompteUtils;
import org.miage.banque.entities.compte.CompteValidator;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.repositories.ComptesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComptesService {

    private final ComptesRepository comptesRepository;
    private final ClientsService clientsService;

    public Compte getCompte(Long id) {
        return comptesRepository.findById(id).orElseThrow(() -> new CompteNotFoundException("Ce compte n'existe pas."));
    }

    public Compte createCompte(Compte compte, Client client) {
        log.info("Création d'un compte pour le client {}", client.getNom());
        compte.setIBAN(CompteUtils.randomIban(client.getPays()));
        compte =  comptesRepository.save(compte);
        clientsService.addCompteToClient(client, compte);
        return compte;
    }

    public Iterable<? extends Compte> getAllComptes() {
        return comptesRepository.findAll();
    }

    public void deleteCompte(Long id) {
        comptesRepository.deleteById(id);
    }

}
