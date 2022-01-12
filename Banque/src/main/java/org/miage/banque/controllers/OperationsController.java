package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.assemblers.OperationsAssembler;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.services.OperationsService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "operations",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Operation.class)
@RequiredArgsConstructor
@Slf4j
public class OperationsController {

    private final OperationsService operationsService;
    private final OperationsAssembler operationsAssembler;

    @GetMapping(value = "/{operationId}")
    public EntityModel<Operation> getOne(@PathVariable("operationId") Long id){
        return operationsAssembler.toModel(operationsService.getOperation(id));
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<EntityModel<Operation>> getAll(){
        return operationsAssembler.toCollectionModel(operationsService.getAll());
    }

    //TODO : Get operations by compte, loop over every cards in the accounts and get operations to return.
}
