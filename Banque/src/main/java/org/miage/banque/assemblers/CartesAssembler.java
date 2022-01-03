package org.miage.banque.assemblers;

import org.hibernate.EntityMode;
import org.miage.banque.entities.carte.Carte;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CartesAssembler implements RepresentationModelAssembler<Carte, EntityModel<Carte>> {

    @Override
    public EntityModel<Carte> toModel(Carte entity) {
        return null;
    }

    @Override
    public CollectionModel<EntityModel<Carte>> toCollectionModel(Iterable<? extends Carte> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
