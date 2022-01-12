package org.miage.banque.assemblers;

import org.miage.banque.controllers.ComptesController;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OperationsAssembler implements RepresentationModelAssembler<Operation, EntityModel<Operation>> {

    @Override
    public EntityModel<Operation> toModel(Operation entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ComptesController.class)
                        .getOne(entity.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<Operation>> toCollectionModel(Iterable<? extends Operation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
