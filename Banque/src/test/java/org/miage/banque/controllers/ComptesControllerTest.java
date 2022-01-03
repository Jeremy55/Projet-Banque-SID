package org.miage.banque.controllers;

import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.services.ComptesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.restassured.RestAssured;
import org.miage.banque.repositories.ComptesRepository;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ComptesControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ComptesService comptesService;

    @BeforeEach
    public void setupContext(){
        RestAssured.port = port;
    }

    @Test
    public void pingApi(){
        when().get("/comptes").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getNotFound(){
        when().get("/comptes/0").then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getOne(){
        when().get("/comptes/1").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void addCompte() throws JSONException {
        JSONObject json = new JSONObject()
                .put("iban", "FR111111111111111111111111111")
                .put("solde", "100");

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/comptes")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void addCompteSoldeNegatif() throws JSONException {
        JSONObject json = new JSONObject()
                .put("iban", "FR111111111111111111111111112")
                .put("solde", "-100");

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/comptes")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void addCompteIbanInvalide() throws JSONException {
        JSONObject json = new JSONObject()
                .put("iban", "1")
                .put("solde", "100");

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/comptes")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void addCompteSameIban() throws JSONException{
        JSONObject json = new JSONObject()
                .put("iban", "FR111111111111111111111111113")
                .put("solde", "100");

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/comptes")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/comptes")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void deleteCompte() throws JSONException {
        Compte compte = new Compte();
        compte.setIBAN("FR111111111111111111111111124");
        compte.setSolde(100);
        comptesService.createCompte(compte);

        //Delete the newly created ressource.
        given()
                .contentType("application/json")
                .when()
                .delete("/comptes/"+compte.getId())
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response();

        // It should be impossible to get the deleted ressource.
        given()
                .contentType("application/json")
                .when()
                .get("/comptes/"+compte.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .response();
    }

    @Test
    public void checkClientHateoas(){
        //Check if there is a link to accounts in _links.
        when().get("/comptes/1").then().body("_links.client.href", notNullValue());
    }
}