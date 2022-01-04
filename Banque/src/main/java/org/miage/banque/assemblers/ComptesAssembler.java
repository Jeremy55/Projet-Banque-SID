package org.miage.banque.assemblers;

import org.miage.banque.controllers.ClientsController;
import org.miage.banque.controllers.ComptesController;
import org.miage.banque.entities.compte.Compte;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ComptesAssembler implements RepresentationModelAssembler<Compte, EntityModel<Compte>> {

    @Override
    public EntityModel<Compte> toModel(Compte compte) {
        return EntityModel.of(compte,
                linkTo(methodOn(ComptesController.class)
                        .getOne(compte.getId())).withSelfRel(),
                linkTo(methodOn(ClientsController.class)
                        .getOne(compte.getClient().getId())).withRel("client"));
    }

    @Override
    public CollectionModel<EntityModel<Compte>> toCollectionModel(Iterable<? extends Compte> entities) {
        List<EntityModel<Compte>> comptesModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(comptesModel,
                linkTo(methodOn(ComptesController.class).getAll()).withSelfRel());
    }
}
