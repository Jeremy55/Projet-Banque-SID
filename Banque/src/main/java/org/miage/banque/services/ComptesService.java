package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.repositories.ComptesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComptesService {
    private final ComptesRepository comptesRepository;

    public Compte getCompte(Long id) {
        return comptesRepository.findById(id).orElseThrow(() -> new CompteNotFoundException("Ce compte n'existe pas."));
    }
}
