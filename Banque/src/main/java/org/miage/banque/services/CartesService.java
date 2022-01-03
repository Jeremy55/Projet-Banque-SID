package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.carte.CartesUtils;
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

    public Carte createCarte(Carte carte) {
        Calendar calendar = Calendar.getInstance();
        //If it's a virtual card it's valid for one week, otherwise it's valid for one year.
        if(carte.isVirtuelle()){
            calendar.add(Calendar.WEEK_OF_YEAR,1);
        }else{
            calendar.add(Calendar.YEAR,1);
        }
        carte.setExpiration(calendar.getTime());

        carte.setNumero(CartesUtils.randomString(16));
        carte.setCode(CartesUtils.randomString(4));
        carte.setCryptogramme(CartesUtils.randomString(3));

        return cartesRepository.save(carte);
    }
}
