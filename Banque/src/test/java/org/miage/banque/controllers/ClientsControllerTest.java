package org.miage.banque.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.apache.http.HttpStatus;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ClientsControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    ClientsService clientsService;

    @Autowired
    ComptesService comptesService;

    @BeforeEach
    public void setupContext() {
        clientsService.deleteAll();
        RestAssured.port = port;
    }

    @Test
    public void addClient() {
        Client client1 = new Client();
        client1.setNom("Picard");
        client1.setPrenom("Jérémy");
        client1.setPays("France");
        client1.setNo_passeport("123456789");
        client1.setTelephone("0329916847");
        client1.setEmail("jeremy55200@hotmail.fr");
        client1.setMot_de_passe("azerty123");
        clientsService.createClient(client1);

        Client retrievedClient = clientsService.getClientByEmail("jeremy55200@hotmail.fr");
        assertThat(retrievedClient.getNom(), equalTo("Picard"));
    }

    @Test
    public void addRoleToClient() {
        Client client1 = new Client();
        client1.setNom("Picard");
        client1.setPrenom("Jérémy");
        client1.setPays("France");
        client1.setNo_passeport("123456789");
        client1.setTelephone("0329916847");
        client1.setEmail("jeremy55200@hotmail.fr");
        client1.setMot_de_passe("azerty123");
        clientsService.createClient(client1);

        clientsService.createRole(new Role(null,"ROLE_CLIENT"));

        clientsService.addRoleToClient("jeremy55200@hotmail.fr","ROLE_CLIENT");

        Client retrievedClient = clientsService.getClientByEmail("jeremy55200@hotmail.fr");
        Role role = retrievedClient.getRoles().stream().findFirst().get();
        assertThat(role.getNom(), equalTo("ROLE_CLIENT"));
    }

    @Test
    public void addCompteToClient() {
        Client client1 = new Client();
        client1.setNom("Picard");
        client1.setPrenom("Jérémy");
        client1.setPays("France");
        client1.setNo_passeport("123456789");
        client1.setTelephone("0329916847");
        client1.setEmail("jeremy55200@hotmail.fr");
        client1.setMot_de_passe("azerty123");
        clientsService.createClient(client1);

        Compte compte1 = new Compte();
        compte1.setSolde(100);
        clientsService.createCompte(compte1, client1);
        client1 = clientsService.getClientByEmail("jeremy55200@hotmail.fr");

        assertThat(client1.getCompte().getIBAN().substring(0,2), equalTo("FR"));
    }

}