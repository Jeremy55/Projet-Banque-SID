package org.miage.banque.assemblers;

import org.miage.banque.controllers.ComptesController;
import org.miage.banque.entities.Compte;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ComptesAssembler implements RepresentationModelAssembler<Compte, EntityModel<Compte>> {

    @Override
    public EntityModel<Compte> toModel(Compte compte) {
        return EntityModel.of(compte,
                linkTo(methodOn(ComptesController.class)
                        .getOne(compte.getId())).withSelfRel());
    }
}
