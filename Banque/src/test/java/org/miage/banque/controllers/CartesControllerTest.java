package org.miage.banque.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.miage.banque.assemblers.CartesAssembler;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.repositories.CartesRepository;
import org.miage.banque.services.CartesService;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartesControllerTest {

    @MockBean
    private CartesService cartesService;
    @Mock
    private ComptesService comptesService;
    @Mock
    private ClientsService clientsService;
    @Mock
    private CartesRepository cartesRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll() throws Exception {
        when(cartesRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Carte(), new Carte())));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/cartes/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void anonymousCantGetAll() throws Exception {
        when(cartesRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Carte(), new Carte())));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/cartes/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "jeremy55200@hotmail.com", roles = {"CLIENT"})
    void get() throws Exception {
        Carte carte = new Carte();
        carte.setNumero("123456789");
        when(cartesService.getCarte(1L)).thenReturn(carte);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/cartes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero", Matchers.is("123456789")));
    }

}