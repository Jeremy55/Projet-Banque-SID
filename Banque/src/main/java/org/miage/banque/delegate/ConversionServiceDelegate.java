package org.miage.banque.delegate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class ConversionServiceDelegate {

    @Autowired
    @Lazy
    RestTemplate restTemplate;

    public Double callConversionService(String from, String to, double quantity) {
        log.info("Utilisation de l'API externe pour convertir {} en {} pour un montant de {}.",from,to,quantity);

        String url = "http://conversion-service/convertion/from/{from}/to/{to}/quantity/{quantity}";

        ResponseEntity<String> response = restTemplate.getForEntity(
                url,
                String.class,
                from,to,quantity);

        double converted =  Double.parseDouble(Objects.requireNonNull(response.getBody()));
        log.info("Résultat de la conversion : {}", converted);
        return converted;
    }

}
