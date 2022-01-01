package org.miage.banque.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.services.ComptesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.restassured.RestAssured;
import org.miage.banque.repositories.ComptesRepository;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ComptesControllerTest {

    @LocalServerPort
    private int port;

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
}