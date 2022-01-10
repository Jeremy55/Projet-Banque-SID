package org.miage.conversion.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="convertion",produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviseConversionController {

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<Double> convert(@PathVariable String from, @PathVariable String to, @PathVariable Double quantity) {
        return ResponseEntity.ok(quantity*1.2);
    }

}
