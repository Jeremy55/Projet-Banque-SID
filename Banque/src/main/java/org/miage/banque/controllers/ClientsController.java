package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.ClientsAssembler;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.client.ClientInput;
import org.miage.banque.services.ClientsService;
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
        clientToCreate.setMot_de_passe(client.getMot_de_passe());
        clientToCreate.setEmail(client.getEmail());
        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.createClient(clientToCreate)), HttpStatus.CREATED);
    }

    @PostMapping(value="/roles")
    @Transactional
    public ResponseEntity<?> createRole(@RequestBody Role role){
        clientsService.createRole(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping({ "/{clientId}/roles" })
    @Transactional
    public ResponseEntity<?> addRole(@PathVariable("clientId") Long clientId, @RequestBody String role) {
        //clientsService.addRoleToClient(clientId, role);
        return new ResponseEntity<>(HttpStatus.OK);
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
        clientToUpdate.setMot_de_passe(client.getMot_de_passe());

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
        if(client.getMot_de_passe() != null) clientToUpdate.setMot_de_passe(client.getMot_de_passe());

        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.updateClient(clientToUpdate)), HttpStatus.OK);
    }
}
