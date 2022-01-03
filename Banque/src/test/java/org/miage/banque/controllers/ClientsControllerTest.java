package org.miage.banque.controllers;

import io.restassured.RestAssured;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientsControllerTest {

    @Autowired
    ComptesService comptesService;

    @Autowired
    ClientsService clientsService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setupContext(){
        RestAssured.port = port;
    }

    @Test
    public void pingApi(){
        when().get("/clients").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getNotFound(){
        when().get("/clients/0").then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getOne(){
        when().get("/clients/1").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkCompteHateoas(){
        //Check if there is a link to accounts in _links.
        when().get("/clients/1").then().body("_links.compte.href", notNullValue());
    }

    @Test
    public void createClientWithExistingAccount() throws JSONException {
        Compte compte = new Compte();
        compte.setIBAN("FR111111111111111111111111121");
        compte.setSolde(500);
        comptesService.createCompte(compte);

        JSONObject jsonClient = new JSONObject()
                .put("nom","Picard")
                .put("prenom","Jérémy")
                .put("pays","France")
                .put("no_passport","123456788")
                .put("telephone","0329874155")
                .put("secret","azerty123")
                .put("compte_id",compte.getId());

        given()
                .contentType("application/json")
                .body(jsonClient.toString())
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

    }

    @Test
    public void createClientWithFakeAccount() throws JSONException {

        JSONObject jsonClient = new JSONObject()
                .put("nom","Picard")
                .put("prenom","Jérémy")
                .put("pays","France")
                .put("no_passport","123456787")
                .put("telephone","0329874155")
                .put("secret","azerty123")
                .put("compte_id","77777777"); //Fake account id.

        given()
                .contentType("application/json")
                .body(jsonClient.toString())
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void putClient() throws JSONException {


        Compte compte = new Compte();
        compte.setIBAN("FR111111111111111111111113222");
        compte.setSolde(500);

        Client client = new Client();
        client.setNom("Picard");
        client.setPrenom("Jérémy");
        client.setPays("France");
        client.setNo_passeport("123456888");
        client.setTelephone("0695198754");
        client.setSecret("azerty123456");
        client.setCompte(compte);

        clientsService.createClient(client);

        JSONObject updatedClient = new JSONObject()
                .put("nom","Picard")
                .put("prenom","Florian")
                .put("pays","France")
                .put("no_passport","123456888")
                .put("telephone","0695198754")
                .put("secret","azerty123456")
                .put("compte_id",compte.getId()); //Fake account id.

        given()
                .contentType("application/json")
                .body(updatedClient.toString())
                .when()
                .put("/clients/"+client.getId())
                .then()
                .statusCode(HttpStatus.SC_OK);

        assertEquals("Florian",clientsService.getClient(client.getId()).getPrenom());
    }

    @Test
    public void createClientDuplicatePassportNumber() throws JSONException {

        Compte compte = new Compte();
        compte.setIBAN("FR111111111111111111111111211");
        compte.setSolde(500);
        comptesService.createCompte(compte);

        Compte compte2 = new Compte();
        compte.setIBAN("FR111111111111111111111111221");
        compte.setSolde(700);
        comptesService.createCompte(compte2);


        JSONObject jsonClient = new JSONObject()
                .put("nom","Picard")
                .put("prenom","Jérémy")
                .put("pays","France")
                .put("no_passport","111111111")
                .put("telephone","0329874155")
                .put("secret","azerty123")
                .put("compte_id",compte.getId());

        JSONObject jsonClient2 = new JSONObject()
                .put("nom","Picard")
                .put("prenom","Jérémy")
                .put("pays","France")
                .put("no_passport","111111111")
                .put("telephone","0329874155")
                .put("secret","azerty123")
                .put("compte_id",compte2.getId());

        given()
                .contentType("application/json")
                .body(jsonClient.toString())
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .contentType("application/json")
                .body(jsonClient2.toString())
                .when()
                .post("/clients")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void deleteClient(){
        Compte compte = new Compte();
        compte.setIBAN("FR111111111111111111111133222");
        compte.setSolde(500);

        Client client = new Client();
        client.setNom("Picard");
        client.setPrenom("Jérémy");
        client.setPays("France");
        client.setNo_passeport("123556888");
        client.setTelephone("0695198754");
        client.setSecret("azerty123456");
        client.setCompte(compte);

        clientsService.createClient(client);
        comptesService.createCompte(compte);

        when().delete("/clients/"+client.getId()).then().statusCode(HttpStatus.SC_NO_CONTENT);
        when().get("/comptes/"+client.getCompte().getId()).then().statusCode(HttpStatus.SC_NOT_FOUND);
        when().get("/clients/"+client.getCompte().getId()).then().statusCode(HttpStatus.SC_NOT_FOUND);

    }

}