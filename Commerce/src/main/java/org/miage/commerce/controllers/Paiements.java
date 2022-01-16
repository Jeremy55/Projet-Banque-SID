package org.miage.commerce.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.commerce.delegate.BanqueDelegate;
import org.miage.commerce.entities.PaiementInput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="paiements",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class Paiements {

    private final BanqueDelegate banqueDelegate;

    @PostMapping
    public void paiement(@RequestBody PaiementInput paiementInput){
        banqueDelegate.callBanquePaiement(paiementInput);
    }
}
