package org.miage.banque.assemblers;

import org.hibernate.EntityMode;
import org.miage.banque.controllers.CartesController;
import org.miage.banque.entities.carte.Carte;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CartesAssembler implements RepresentationModelAssembler<Carte, EntityModel<Carte>> {

    @Override
    public EntityModel<Carte> toModel(Carte carte) {
        return EntityModel.of(carte,
                linkTo(methodOn(CartesController.class)
                        .getOne(carte.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<Carte>> toCollectionModel(Iterable<? extends Carte> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
