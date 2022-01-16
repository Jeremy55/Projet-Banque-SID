package org.miage.banque.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.compte.CompteInput;
import org.miage.banque.repositories.ClientsRepository;
import org.miage.banque.repositories.ComptesRepository;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ComptesControllerTest {

    @MockBean
    private ComptesService comptesService;
    @Mock
    private ComptesRepository comptesRepository;
    @Mock
    private ClientsService clientsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll() throws Exception {
        when(comptesRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Compte(), new Compte())));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/comptes/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void aonymousUserCantGet() throws Exception {
        when(comptesRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Compte(), new Compte())));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/comptes/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "jeremy55200@hotmail.com", roles = {"CLIENT"})
    void get() throws Exception {
        Compte compte = new Compte();
        compte.setIBAN("FR763000200550000010100000101");
        Client client = new Client();
        client.setEmail("jeremy55200@hotmail.com");
        compte.setClient(client);
        when(comptesService.getCompte(1L)).thenReturn(compte);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/comptes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", Matchers.is("FR763000200550000010100000101")));
    }

    @Test
    @WithMockUser(username = "jeremy55200@hotmail.com", roles = {"CLIENT"})
    void notAllowedGet() throws Exception {
        Compte compte = new Compte();
        compte.setIBAN("FR763000200550000010100000101");
        Client client = new Client();
        client.setEmail("florian55200@hotmail.com");
        compte.setClient(client);
        when(comptesService.getCompte(1L)).thenReturn(compte);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/comptes/1"))
                .andExpect(status().is4xxClientError());
    }

}