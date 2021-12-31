package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.ComptesAssembler;
import org.miage.banque.entities.Compte;
import org.miage.banque.services.ComptesService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping(value = "comptes",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Compte.class)
@RequiredArgsConstructor

public class ComptesController {

    private final ComptesService comptesService;
    private final ComptesAssembler comptesAssembler;

    @GetMapping(value="/{compteId}")
    public EntityModel<Compte> getOne(@PathVariable("compteId")  Long id){
        return comptesAssembler.toModel(comptesService.getCompte(id));
    }



}
