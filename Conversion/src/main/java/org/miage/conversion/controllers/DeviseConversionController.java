package org.miage.conversion.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="convertion",produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DeviseConversionController {

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<Double> convert(@PathVariable String from, @PathVariable String to, @PathVariable Double quantity) {
        log.info("Convertion de {} à {} pour un montant de {}", from, to, quantity);
        return ResponseEntity.ok(quantity*1.2);
    }

}
