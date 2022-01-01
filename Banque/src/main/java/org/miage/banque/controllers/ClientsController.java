package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.ClientsAssembler;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.client.ClientInput;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.compte.CompteInput;
import org.miage.banque.services.ClientsService;
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
@RequestMapping(value="clients",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(ClientsController.class)
@RequiredArgsConstructor
public class ClientsController {

    private final ClientsService clientsService;
    private final ComptesService comptesService;
    private final ClientsAssembler clientsAssembler;

    @GetMapping(value="/{clientId}")
    public EntityModel<Client> getOne(@PathVariable("clientId") Long clientId) {
        return clientsAssembler.toModel(clientsService.getClient(clientId));
    }

    @GetMapping
    public Iterable<EntityModel<Client>> getAll(){
        return clientsAssembler.toCollectionModel(clientsService.getAllClients());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid ClientInput client){
        Client clientToCreate = new Client();
        clientToCreate.setNom(client.getNom());
        clientToCreate.setPrenom(client.getPrenom());
        clientToCreate.setPays(client.getPays());
        clientToCreate.setNo_passeport(client.getNo_passport());
        clientToCreate.setTelephone(client.getTelephone());
        clientToCreate.setSecret(client.getSecret());
        //Retrieve an existing account to link it to the newly created client.
        clientToCreate.setCompte(comptesService.getCompte(client.getCompte_id()));
        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.createClient(clientToCreate)), HttpStatus.CREATED);
    }

}
