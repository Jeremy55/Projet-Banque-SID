package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.miage.banque.entities.Compte;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.repositories.ComptesRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ComptesService {
    private final ComptesRepository comptesRepository;

    public Compte getCompte(Long id) {
        return comptesRepository.findById(id).orElseThrow(CompteNotFoundException::new);
    }
}
