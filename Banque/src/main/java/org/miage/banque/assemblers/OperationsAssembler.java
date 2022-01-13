package org.miage.banque.assemblers;

import org.miage.banque.controllers.CartesController;
import org.miage.banque.controllers.ComptesController;
import org.miage.banque.controllers.OperationsController;
import org.miage.banque.entities.client.Client;
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
        EntityModel<Operation> operationModel = EntityModel.of(entity);
        operationModel.add(linkTo(methodOn(OperationsController.class).getOne(entity.getId())).withSelfRel());
        operationModel.add(linkTo(methodOn(CartesController.class).getOne(entity.getCarte().getId())).withRel("carte"));
        return operationModel;
    }

    @Override
    public CollectionModel<EntityModel<Operation>> toCollectionModel(Iterable<? extends Operation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
