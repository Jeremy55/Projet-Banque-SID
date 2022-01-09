package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.assemblers.ComptesAssembler;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.compte.CompteInput;
import org.miage.banque.exceptions.InvalidTokenException;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "comptes",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Compte.class)
@RequiredArgsConstructor

@Slf4j
public class ComptesController {

    private final ComptesService comptesService;
    private final ClientsService clientsService;
    private final ComptesAssembler comptesAssembler;

    @GetMapping(value="/{compteId}")
    public EntityModel<Compte> getOne(@PathVariable("compteId")  Long id){
        return comptesAssembler.toModel(comptesService.getCompte(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<EntityModel<Compte>> getAll(){
        return comptesAssembler.toCollectionModel(comptesService.getAllComptes());
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> create(@RequestBody @Valid CompteInput compte, @AuthenticationPrincipal String mailClient){
        log.info("Tentative de création d'un compte pour le client authentifié  avec le mail {}", mailClient);

        Compte compteToCreate = new Compte();
        compteToCreate.setSolde(compte.getSolde());

        Client client = clientsService.getClientByEmail(mailClient);

        return new ResponseEntity<>(comptesAssembler.toModel(comptesService.createCompte(compteToCreate,client)), HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{compteId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("compteId") Long id){
        comptesService.deleteCompte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
