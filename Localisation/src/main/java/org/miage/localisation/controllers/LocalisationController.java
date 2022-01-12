package org.miage.localisation.controllers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@RestController
@RequestMapping(value="localisation",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class LocalisationController {

    @Autowired
    @Lazy
    RestTemplate restTemplate;

    @RequestMapping("/{latitude}/{longitude}/country")
    public String getCountry(@PathVariable String latitude, @PathVariable String longitude) throws ParseException {
        log.info("Obtenir le pays de la localisation {} {}", latitude, longitude);

        String url = "http://api.geonames.org/countryCodeJSON?lat={lat}&lng={long}&username=jeremy552";

        ResponseEntity<String> response = restTemplate.getForEntity(
                url,
                String.class,
                latitude,longitude);

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.getBody());
        return Objects.requireNonNull(jsonObject.get("countryCode")).toString();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
