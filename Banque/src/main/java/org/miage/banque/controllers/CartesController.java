package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.CartesAssembler;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.carte.CarteInput;
import org.miage.banque.entities.client.Client;
import org.miage.banque.services.CartesService;
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
@RequestMapping(value="cartes",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Carte.class)
@RequiredArgsConstructor
public class CartesController  {

    private final CartesAssembler cartesAssembler;
    private final CartesService cartesService;
    private final ComptesService comptesService;

    @GetMapping(value = "/{carteId}")
    public EntityModel<Carte> getOne(@PathVariable("carteId") Long carteId) {
        return cartesAssembler.toModel(cartesService.getCarte(carteId));
    }

    @GetMapping
    public Iterable<EntityModel<Carte>> getAll(){
        return cartesAssembler.toCollectionModel(cartesService.getAllCartes());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid CarteInput carteInput) {
        Carte carte = new Carte();
        carte.setActive(carteInput.isActive());
        carte.setContact(carteInput.isContact());
        carte.setVirtuelle(carteInput.isVirtuelle());
        carte.setLocalisation(carteInput.isLocalisation());
        carte.setPlafond(carteInput.getPlafond());
        //Retrieve the compte to link the carte to.
        carte.setCompte(comptesService.getCompte(carteInput.getCompte_id()));

        return new ResponseEntity<>(cartesAssembler.toModel(cartesService.createCarte(carte)), HttpStatus.CREATED);
    }


}
