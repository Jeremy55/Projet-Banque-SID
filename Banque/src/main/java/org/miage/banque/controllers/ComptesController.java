package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.Compte;
import org.miage.banque.services.ComptesService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "comptes",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Compte.class)
@RequiredArgsConstructor

public class ComptesController {

    private final ComptesService comptesService;

    @GetMapping(value="/{compteId}")
    public Compte getOne(@PathVariable("compteId")  Long id){
        return comptesService.getCompte(id);
    }


}
