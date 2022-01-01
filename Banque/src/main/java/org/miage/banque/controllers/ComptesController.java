package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.ComptesAssembler;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.compte.CompteInput;
import org.miage.banque.services.ComptesService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import javax.validation.Valid;

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

    @GetMapping
    public Iterable<EntityModel<Compte>> getAll(){
        return comptesAssembler.toCollectionModel(comptesService.getAllComptes());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid CompteInput compte){
        Compte compteToCreate = new Compte();
        compteToCreate.setIBAN(compte.getIBAN());
        compteToCreate.setSolde(compte.getSolde());
        return new ResponseEntity<>(comptesAssembler.toModel(comptesService.createCompte(compteToCreate)), HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{compteId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("compteId") Long id){
        comptesService.deleteCompte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
