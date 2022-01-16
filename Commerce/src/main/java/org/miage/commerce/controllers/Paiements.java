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

    @PostMapping(value = "/contact")
    public String paiementSansContact(@RequestBody PaiementInput paiementInput){
        paiementInput.setLibelle("Auchan");
        paiementInput.setCategorie("Auchan - Débit - Sans contact");
        paiementInput.setPays("France");
        paiementInput.setDevise("EUR");
        paiementInput.setContact(true);
        paiementInput.setOnline(false);
        paiementInput.setLatitude("2");
        paiementInput.setLongitude("48");
        paiementInput.setIBAN_debiteur("FR7630002005501234567890185");
        return banqueDelegate.callBanquePaiement(paiementInput);
    }

    @PostMapping(value = "/online")
    public String paiementOnline(@RequestBody PaiementInput paiementInput){
        paiementInput.setLibelle("Auchan");
        paiementInput.setCategorie("Auchan - Débit - Online");
        paiementInput.setPays("France");
        paiementInput.setDevise("EUR");
        paiementInput.setContact(false);
        paiementInput.setOnline(true);
        paiementInput.setLatitude("2");
        paiementInput.setLongitude("48");
        paiementInput.setIBAN_debiteur("FR7630002005501234567890185");
        return banqueDelegate.callBanquePaiement(paiementInput);
    }

    @PostMapping
    public String paiement(@RequestBody PaiementInput paiementInput){
        paiementInput.setLibelle("Auchan");
        paiementInput.setCategorie("Auchan - Débit");
        paiementInput.setPays("France");
        paiementInput.setDevise("EUR");
        paiementInput.setContact(false);
        paiementInput.setOnline(false);
        paiementInput.setLatitude("2");
        paiementInput.setLongitude("48");
        paiementInput.setIBAN_debiteur("FR7630002005501234567890185");
        return banqueDelegate.callBanquePaiement(paiementInput);
    }
}
