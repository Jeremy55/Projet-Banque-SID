package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.carte.CarteUtils;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.exceptions.CarteNotFoundException;
import org.miage.banque.repositories.CartesRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class CartesService {

    private final CartesRepository cartesRepository;

    public Carte getCarte(Long id) {
        return cartesRepository.findById(id).orElseThrow(() -> new CarteNotFoundException("Carte non trouvée."));
    }

    public Carte createCarte(Carte carte, Compte compte) {
        Calendar calendar = Calendar.getInstance();
        //If it's a virtual card it's valid for two weeks, otherwise it's valid for one year.
        if(carte.isVirtuelle()){
            calendar.add(Calendar.WEEK_OF_YEAR,2);
        }else{
            calendar.add(Calendar.YEAR,2);
        }
        carte.setExpiration(calendar.getTime());

        carte.setNumero(CarteUtils.generateNumeroCarte());
        carte.setCode(CarteUtils.generateCodeCarte());
        carte.setCryptogramme(CarteUtils.generateCryptogramme());

        carte.setCompte(compte);
        return cartesRepository.save(carte);
    }

    public Carte getCarteByNumero(String numero) {
        return cartesRepository.findByNumero(numero);
    }

    public Iterable<? extends Carte> getAllCartes() {
        return cartesRepository.findAll();
    }

    public void deleteCarte(Carte carte) {
        carte.setActive(false);
        cartesRepository.save(carte);
    }
}
