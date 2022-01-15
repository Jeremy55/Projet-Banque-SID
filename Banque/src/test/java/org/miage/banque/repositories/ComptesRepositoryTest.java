package org.miage.banque.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.compte.Compte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ComptesRepositoryTest {

    @Autowired
    private ComptesRepository comptesRepository;

    @AfterEach
    void tearDown() {
        comptesRepository.deleteAll();
    }

    @Test
    void compte_exists_findCompteByIBAN() {
        Compte compte = new Compte();
        compte.setIBAN("FR763000200550000010100000101");
        compte.setSolde(100);
        comptesRepository.save(compte);
        Compte compte1 = comptesRepository.findCompteByIBAN("FR763000200550000010100000101");
        assertThat(compte1.getIBAN()).isEqualTo(compte.getIBAN());
    }

    @Test
    void compte_not_exists_findCompteByIBAN() {
        Compte compte = new Compte();
        compte.setIBAN("FR763000200550000010100000101");
        compte.setSolde(100);
        comptesRepository.save(compte);
        Compte compte1 = comptesRepository.findCompteByIBAN("FR763000200550000010100000102");
        assertThat(compte1).isNull();
    }
}