package org.miage.banque.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.miage.banque.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RolesRepositoryTest {

    @Autowired
    RolesRepository rolesRepository;

    @AfterEach
    void tearDown() {
        rolesRepository.deleteAll();
    }

    @Test
    void role_exists_findByNom() {
        Role role = new Role();
        role.setNom("ROLE_ADMIN");
        rolesRepository.save(role);
        Role role1 = rolesRepository.findByNom("ROLE_ADMIN");
        assertThat(role.getNom()).isEqualTo(role1.getNom());
    }

    @Test
    void role_not_exists_findByNom() {
        Role role = new Role();
        role.setNom("ROLE_ADMIN");
        rolesRepository.save(role);
        Role role1 = rolesRepository.findByNom("ROLE_USER");
        assertThat(role1).isNull();
    }
}