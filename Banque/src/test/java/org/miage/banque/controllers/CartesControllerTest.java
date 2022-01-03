package org.miage.banque.controllers;

import io.restassured.RestAssured;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.services.CartesService;
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
class CartesControllerTest {

    @Autowired
    CartesService cartesService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setupContext(){
        RestAssured.port = port;
    }

    @Test
    public void pingApi() {
        when().
            get("/cartes").
        then().
            statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getNotFound() {
        when().
            get("/cartes/0").
        then().
            statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getOne() {
        when().
            get("/cartes/1").
        then().
            statusCode(HttpStatus.SC_OK);
    }
}