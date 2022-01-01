package org.miage.banque.assemblers;


import org.miage.banque.controllers.ClientsController;
import org.miage.banque.controllers.ComptesController;
import org.miage.banque.entities.client.Client;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientsAssembler implements RepresentationModelAssembler<Client, EntityModel<Client>> {
    @Override
    public EntityModel<Client> toModel(Client client) {
        return EntityModel.of(client,
                linkTo(methodOn(ClientsController.class)
                        .getOne(client.getId())).withSelfRel(),
                linkTo(methodOn(ComptesController.class)
                        .getOne(client.getCompte().getId())).withRel("compte"));
    }
}
