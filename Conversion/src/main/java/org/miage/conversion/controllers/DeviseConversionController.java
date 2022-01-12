package org.miage.conversion.controllers;

import lombok.extern.slf4j.Slf4j;
import org.miage.conversion.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="convertion",produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DeviseConversionController {

    Converter converter = new Converter();

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<Double> convert(@PathVariable String from, @PathVariable String to, @PathVariable Double quantity) {
        log.info("Convertion de {} à {} pour un montant de {}", from, to, quantity);
        double rate = converter.getConversionRate(from, to);
        log.info("Le taux de conversion est {}", rate);
        return ResponseEntity.ok(quantity*rate);
    }

    @GetMapping("supported-currencies")
    public ResponseEntity<?> supported(){
        return ResponseEntity.ok(converter.getSupportedPairs());
    }

}
