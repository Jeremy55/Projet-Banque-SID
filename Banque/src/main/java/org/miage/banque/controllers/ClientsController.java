package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.ClientsAssembler;
import org.miage.banque.entities.client.Client;
import org.miage.banque.services.ClientsService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
