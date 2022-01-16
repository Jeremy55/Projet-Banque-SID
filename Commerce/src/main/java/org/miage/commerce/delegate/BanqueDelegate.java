package org.miage.commerce.delegate;

import lombok.extern.slf4j.Slf4j;
import org.miage.commerce.entities.PaiementInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BanqueDelegate {

    @Autowired
    @Lazy
    RestTemplate restTemplate;

    public String callBanquePaiement(PaiementInput paiementInput) {
        log.info("Utilisation de l'API de la banque pour recevoir un paiement de {}." , paiementInput.getMontant());

        String url = "http://banque-service:8081/operations/";

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                paiementInput,
                String.class);

        return response.getBody();
    }

}
