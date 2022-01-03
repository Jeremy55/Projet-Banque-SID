package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.repositories.CartesRepository;
import org.miage.banque.repositories.ClientsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartesServices {

    private final CartesRepository cartesRepository;

    public Carte getCarte(Long id) {
        return cartesRepository.findById(id).orElseThrow(() -> new RuntimeException("Carte non trouvée."));
    }
}
