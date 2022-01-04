package org.miage.banque.assemblers;


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
        if(client.getCompte() != null) {
            return EntityModel.of(client,
                    linkTo(methodOn(ClientsController.class)
                            .getOne(client.getId())).withSelfRel(),
                            linkTo(methodOn(ComptesController.class)
                            .getOne(client.getCompte().getId())).withRel("compte"));
        }
        return EntityModel.of(client,
                linkTo(methodOn(ClientsController.class)
                        .getOne(client.getId())).withSelfRel());
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
