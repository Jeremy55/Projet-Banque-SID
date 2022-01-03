package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.carte.CarteInput;
import org.miage.banque.exceptions.CarteNotFoundException;
import org.miage.banque.repositories.CartesRepository;
import org.miage.banque.repositories.ClientsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartesService {

    private final CartesRepository cartesRepository;

    public Carte getCarte(Long id) {
        return cartesRepository.findById(id).orElseThrow(() -> new CarteNotFoundException("Carte non trouvée."));
    }

    public Carte createCarte(Carte carte) {
        return cartesRepository.save(carte);
    }
}
