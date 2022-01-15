package org.miage.banque.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientsRepositoryTest {
    @Autowired
    ClientsRepository clientsRepository;

    @AfterEach
    void tearDown() {
        clientsRepository.deleteAll();
    }

    @Test
    void client_exists_findByEmail() {
        Client client = new Client();
        client.setNom("Picard");
        client.setEmail("jeremy55200@hotmail.com");

        clientsRepository.save(client);

        assertThat(clientsRepository.findByEmail("jeremy55200@hotmail.com").getNom()).isEqualTo(client.getNom());
    }

    @Test
    void client_not_exists_findByEmail() {
        Client client = clientsRepository.findByEmail("jeremy55200@hotmail.com");
        assertThat(client).isNull();
    }
}