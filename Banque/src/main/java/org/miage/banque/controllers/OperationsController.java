package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.assemblers.OperationsAssembler;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.entities.operation.OperationCarteInput;
import org.miage.banque.exceptions.CarteNotFoundException;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.exceptions.InvalidTokenException;
import org.miage.banque.services.CartesService;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.miage.banque.services.OperationsService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping(value = "operations",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Operation.class)
@RequiredArgsConstructor
@Slf4j
public class OperationsController {

    private final OperationsService operationsService;
    private final CartesService cartesService;
    private final ClientsService clientsService;
    private final ComptesService comptesService;
    private final OperationsAssembler operationsAssembler;

    @GetMapping(value = "/{operationId}")
    public EntityModel<Operation> getOne(@PathVariable("operationId") Long id){
        return operationsAssembler.toModel(operationsService.getOperation(id));
    }

    @PostMapping()
    public EntityModel<Operation> create(@RequestBody OperationCarteInput operationCarteInput){
        //Creating the operation from the input.
        Operation operation = new Operation();
        operation.setLibelle(operationCarteInput.getLibelle());
        operation.setMontant(operationCarteInput.getMontant());
        operation.setCategorie(operationCarteInput.getCategorie());
        operation.setPays(operationCarteInput.getPays());
        operation.setIBAN_debiteur(operationCarteInput.getIBAN_debiteur());
        operation.setDevise(operationCarteInput.getDevise());
        operation.setContact(operationCarteInput.isContact());
        operation.setOnline(operationCarteInput.isOnline());
        Carte carte = cartesService.getCarteByNumero(operationCarteInput.getNumero());
        operation.setCarte(carte);

        if(carte.isLocalisation()){
            operation.setLatitude(operationCarteInput.getLatitude());
            operation.setLongitude(operationCarteInput.getLongitude());
        }

        //If the card accept contactless payment and the amount is less than 50 we don't need to check anything else.
        if(carte.isContact() && operation.isContact() && operation.getMontant() <= 50){
            return operationsAssembler.toModel(operationsService.create(operation));
        }

        //If the code is wrong we stop here.
        if(!Objects.equals(carte.getCode(), operationCarteInput.getCode())){
            throw new RuntimeException("Code de carte invalide");
        }

        // If it's an online payment the cryptogram must be checked.
        if(operation.isOnline() && !operationCarteInput.getCryptogramme().equals(carte.getCryptogramme())){
            throw new RuntimeException("Cryptogramme invalide");
        }

        return operationsAssembler.toModel(operationsService.create(operation));
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<EntityModel<Operation>> getAll(){
        return operationsAssembler.toCollectionModel(operationsService.getAll());
    }

    @GetMapping("/compte/{compteId}")
    public Iterable<EntityModel<Operation>> getAllByCompte(@PathVariable("compteId") Long compteId, @AuthenticationPrincipal String email){
        log.info("Vérification du compte de l'utilisateur {}", email);
        Compte compte = clientsService.getClientByEmail(email).getCompte();

        if(compte == null){
            throw new CompteNotFoundException("Vous n'avez pas de compte.");
        }

        if(!compte.getId().equals(compteId)){
            throw new InvalidTokenException("Ce compte ne vous appartient pas.");
        }
        Collection<Operation> operations = new ArrayList<>();
        compte.getCartes().forEach(carte -> operations.addAll(carte.getOperations()));
        return operationsAssembler.toCollectionModel(operations);
    }

    @GetMapping("/carte/{carteId}")
    public Iterable<EntityModel<Operation>> getAllByCarte(@PathVariable("carteId") Long carteId, @AuthenticationPrincipal String email){
        log.info("Vérification que la carte {} appartient bien à l'utilisateur {}", carteId, email);
        Compte compte = clientsService.getClientByEmail(email).getCompte();

        if(compte == null){
            throw new CarteNotFoundException("Vous n'avez pas de compte.");
        }

        Carte carte = cartesService.getCarte(carteId);

        if(!comptesService.carteBelongsToCompte(compte,carte)){
            throw new InvalidTokenException("Cette carte ne vous appartient pas.");
        }

        return operationsAssembler.toCollectionModel(carte.getOperations());
    }

}
