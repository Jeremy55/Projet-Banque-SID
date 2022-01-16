package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.CartesAssembler;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.carte.CarteInput;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.services.CartesService;
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
@RequestMapping(value="cartes",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Carte.class)
@RequiredArgsConstructor
public class CartesController  {

    private final CartesAssembler cartesAssembler;
    private final CartesService cartesService;
    private final ComptesService comptesService;
    private final ClientsService clientsService;

    @GetMapping(value = "/{carteId}")
    public EntityModel<Carte> getOne(@PathVariable("carteId") Long carteId) {
        return cartesAssembler.toModel(cartesService.getCarte(carteId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<EntityModel<Carte>> getAll(){
        return cartesAssembler.toCollectionModel(cartesService.getAllCartes());
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> create(@RequestBody @Valid CarteInput carteInput, @AuthenticationPrincipal String clientEmail) {
        Carte carte = new Carte();
        carte.setActive(carteInput.isActive());
        carte.setContact(carteInput.isContact());
        carte.setVirtuelle(carteInput.isVirtuelle());
        carte.setLocalisation(carteInput.isLocalisation());
        carte.setPlafond(carteInput.getPlafond());

        Compte compte = clientsService.getClientByEmail(clientEmail).getCompte();

        return new ResponseEntity<>(cartesAssembler.toModel(cartesService.createCarte(carte,compte)), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{carteId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> delete(@PathVariable Long carteId, @AuthenticationPrincipal String clientEmail) {
        Compte compte = clientsService.getClientByEmail(clientEmail).getCompte();
        Carte carte = cartesService.getCarte(carteId);
        if(Objects.equals(carte.getCompte().getId(), compte.getId())){
            cartesService.deleteCarte(carte);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new RuntimeException("Cette Carte ne vous appartient pas.");
    }
}
