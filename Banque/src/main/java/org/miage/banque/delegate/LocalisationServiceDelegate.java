package org.miage.banque.delegate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class LocalisationServiceDelegate {

    @Autowired
    @Lazy
    RestTemplate restTemplate;

    public String callLocalisationService(String longitude, String latitude){
        log.info("Obtention du pays des coordonées longitude : {} , latitude : {}",longitude,latitude);
        String url = "http://localisation-service/localisation/{longitude}/{latitude}/country";

        ResponseEntity<String> response = restTemplate.getForEntity(
                url,
                String.class,
                longitude,latitude);

        String countryCode = response.getBody();
        log.info("Le pays est : {}",countryCode);
        return countryCode;
    }

}
