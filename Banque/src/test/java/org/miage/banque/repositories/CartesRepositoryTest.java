package org.miage.banque.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miage.banque.BanqueApplication;
import org.miage.banque.entities.carte.Carte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartesRepositoryTest {

    @Autowired
    private CartesRepository cartesRepository;

    @AfterEach
    void tearDown() {
        cartesRepository.deleteAll();
    }

    @Test
    void carte_existe_findByNumero() {
        Carte carte = new Carte();
        carte.setNumero("1234567890123456");
        cartesRepository.save(carte);

        Carte carte1 = cartesRepository.findByNumero("1234567890123456");

        assertThat(carte1.getNumero()).isEqualTo(carte.getNumero());
    }

    @Test
    void carte_null_findByNumero(){
        Carte carte = cartesRepository.findByNumero("1234567890123456");
        assertThat(carte).isNull();
    }
}