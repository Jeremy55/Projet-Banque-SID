package org.miage.banque.assemblers;


import lombok.NonNull;
import org.miage.banque.controllers.ClientsController;
import org.miage.banque.controllers.ComptesController;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientsAssembler implements RepresentationModelAssembler<Client, EntityModel<Client>> {

    @Override
    public EntityModel<Client> toModel(Client client) {
        //Why is this removing the clients roles ?
        EntityModel<Client> clientModel = EntityModel.of(client);
        clientModel.add(linkTo(methodOn(ClientsController.class).getOne(client.getId())).withSelfRel());
        if(client.getCompte() != null) {
            clientModel.add(linkTo(methodOn(ComptesController.class).getOne(client.getCompte().getId())).withRel("compte"));
        }
        return clientModel;
    }

    @Override
    public CollectionModel<EntityModel<Client>> toCollectionModel(Iterable<? extends Client> entities) {
        List<EntityModel<Client>> comptesModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(comptesModel,
                linkTo(methodOn(ComptesController.class).getAll()).withSelfRel());
    }
}
