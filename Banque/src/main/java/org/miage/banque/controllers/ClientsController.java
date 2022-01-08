package org.miage.banque.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.miage.banque.assemblers.ClientsAssembler;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.client.ClientInput;
import org.miage.banque.services.ClientsService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="clients",produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(ClientsController.class)
@RequiredArgsConstructor
public class ClientsController {

    private final ClientsService clientsService;
    private final ClientsAssembler clientsAssembler;

    @GetMapping(value="/{clientId}")
    public EntityModel<Client> getOne(@PathVariable("clientId") Long clientId) {
        return clientsAssembler.toModel(clientsService.getClient(clientId));
    }

    @GetMapping
    public Iterable<EntityModel<Client>> getAll(){
        return clientsAssembler.toCollectionModel(clientsService.getAllClients());
    }

    @GetMapping(value="/token/rafraichir")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Client client = clientsService.getClientByEmail(username);

                String accessToken = JWT.create()
                        .withSubject(client.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis()  + 10 + 60 * 1000)) //Valid for 10 minutes.
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", client.getRoles().stream().map(Role::getNom).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception e){
                response.setHeader("Erreur",e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else {
            throw new RuntimeException("Refresh Token invalide");
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid ClientInput client){
        Client clientToCreate = new Client();
        clientToCreate.setNom(client.getNom());
        clientToCreate.setPrenom(client.getPrenom());
        clientToCreate.setPays(client.getPays());
        clientToCreate.setNo_passeport(client.getNo_passport());
        clientToCreate.setTelephone(client.getTelephone());
        clientToCreate.setMot_de_passe(client.getMot_de_passe());
        clientToCreate.setEmail(client.getEmail());
        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.createClient(clientToCreate)), HttpStatus.CREATED);
    }

    @PostMapping(value="/roles")
    @Transactional
    public ResponseEntity<?> createRole(@RequestBody Role role){
        clientsService.createRole(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping({ "/{clientId}/roles" })
    @Transactional
    public ResponseEntity<?> addRole(@PathVariable("clientId") Long clientId, @RequestBody String role) {
        //clientsService.addRoleToClient(clientId, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/{clientId}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable("clientId") Long clientId, @RequestBody ClientInput client){

        Client clientToUpdate = clientsService.getClient(clientId);
        clientToUpdate.setNom(client.getNom());
        clientToUpdate.setPrenom(client.getPrenom());
        clientToUpdate.setPays(client.getPays());
        clientToUpdate.setNo_passeport(client.getNo_passport());
        clientToUpdate.setTelephone(client.getTelephone());
        clientToUpdate.setMot_de_passe(client.getMot_de_passe());

        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.updateClient(clientToUpdate)), HttpStatus.OK);
    }

    @PatchMapping(value="/{clientId}")
    @Transactional
    public ResponseEntity<?> patch(@PathVariable("clientId") Long clientId, @RequestBody ClientInput client){
        Client clientToUpdate = clientsService.getClient(clientId);
        if(client.getNom() != null) clientToUpdate.setNom(client.getNom());
        if(client.getPrenom() != null) clientToUpdate.setPrenom(client.getPrenom());
        if(client.getPays() != null) clientToUpdate.setPays(client.getPays());
        if(client.getNo_passport() != null) clientToUpdate.setNo_passeport(client.getNo_passport());
        if(client.getTelephone() != null) clientToUpdate.setTelephone(client.getTelephone());
        if(client.getMot_de_passe() != null) clientToUpdate.setMot_de_passe(client.getMot_de_passe());

        return new ResponseEntity<>(clientsAssembler.toModel(clientsService.updateClient(clientToUpdate)), HttpStatus.OK);
    }
}
