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

    @PutMapping(value="/{clientId}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable("clientId") Long clientId, @RequestBody ClientInput client){

        Client clientToUpdate = clientsService.getClient(clientId);
        clientToUpdate.setNom(client.getNom());
        clientToUpdate.setPrenom(client.getPrenom());
        clientToUpdate.setPays(client.getPays());
        clientToUpdate.setNo_passeport(client.getNo_passport());
        clientToUpdate.setTelephone(client.getTelephone());
        clientToUpdate.setSecret(client.getSecret());

        //Retrieve an existing account to link it to the newly created client.
        clientToUpdate.setCompte(comptesService.getCompte(client.getCompte_id()));

        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.updateClient(clientToUpdate)), HttpStatus.OK);
    }

    @PatchMapping(value="/{clientId}")
    @Transactional
    public ResponseEntity<?> patch(@PathVariable("clientId") Long clientId, @RequestBody ClientInput client){
        Client clientToUpdate = clientsService.getClient(clientId);
        if(client.getNom() != null) clientToUpdate.setNom(client.getNom());
        if(client.getPrenom() != null) clientToUpdate.setPrenom(client.getPrenom());
        if(client.getPays() != null) clientToUpdate.setPays(client.getPays());
        if(client.getNo_passport() != null) clientToUpdate.setNo_passeport(client.getNo_passport());
        if(client.getTelephone() != null) clientToUpdate.setTelephone(client.getTelephone());
        if(client.getSecret() != null) clientToUpdate.setSecret(client.getSecret());

        //Retrieve an existing account to link it to the newly created client.
        if(client.getCompte_id() != null) clientToUpdate.setCompte(comptesService.getCompte(client.getCompte_id()));

        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.updateClient(clientToUpdate)), HttpStatus.OK);
    }

    @DeleteMapping(value="/{clientId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("clientId") Long clientId){
        clientsService.deleteClient(clientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
