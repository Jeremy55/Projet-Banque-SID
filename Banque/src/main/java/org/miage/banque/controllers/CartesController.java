package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.CartesAssembler;
import org.miage.banque.entities.carte.Carte;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="cartes",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Carte.class)
@RequiredArgsConstructor
public class CartesController  {

    private final CartesAssembler cartesAssembler;

}
