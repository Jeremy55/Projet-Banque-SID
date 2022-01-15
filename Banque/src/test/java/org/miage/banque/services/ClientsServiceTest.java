package org.miage.banque.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.exceptions.CarteNotFoundException;
import org.miage.banque.exceptions.ClientNotFoundException;
import org.miage.banque.repositories.CartesRepository;
import org.miage.banque.repositories.ClientsRepository;
import org.miage.banque.repositories.ComptesRepository;
import org.miage.banque.repositories.RolesRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientsServiceTest {

    @Mock
    private ClientsRepository clientsRepository;
    @Mock
    private RolesRepository rolesRepository;
    @Mock
    private ComptesRepository comptesRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    private ClientsService clientsService;

    @BeforeEach
    void setUp() {
        clientsService = new ClientsService(clientsRepository, rolesRepository, comptesRepository,passwordEncoder);
    }

    @Test
    void getClient(){
        Client client = new Client();
        client.setId(1L);
        given(clientsRepository.findById(1L)).willReturn(java.util.Optional.of(client));
        clientsService.getClient(1L);
        verify(clientsRepository).findById(1L);
    }

    @Test
    void willThrowIfClientNotExistsgetClient(){
        assertThatThrownBy(() -> clientsService.getClient(1L))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessage("Ce client n'existe pas.");
    }

    @Test
    void getAllClients(){
        clientsService.getAllClients();
        verify(clientsRepository).findAll();
    }

    @Test
    void addRole(){
        Client client = new Client();
        client.setId(1L);
        client.setEmail("jeremy55200@hotmail.com");
        Role role = new Role();
        role.setNom("ROLE_USER");

        given(clientsRepository.findByEmail("jeremy55200@hotmail.com")).willReturn(client);
        given(rolesRepository.findByNom("ROLE_USER")).willReturn(role);

        clientsService.addRoleToClient("jeremy55200@hotmail.com","ROLE_USER");
        assertThat(client.getRoles().contains(role)).isTrue();
    }

    @Test
    void createClient(){
        Client client = new Client();
        client.setId(1L);
        clientsService.createClient(client);
        verify(clientsRepository).save(client);
    }

    @Test
    void verifyPasswordOfClientIsHashed(){
        Client client = new Client();
        client.setMot_de_passe("password");
        client.setNom("Picard");
        given(passwordEncoder.encode(client.getMot_de_passe())).willReturn("password");
        clientsService.createClient(client);
        verify(passwordEncoder).encode(client.getMot_de_passe());
    }

    @Test
    void updateClient(){
        Client client = new Client();
        client.setId(1L);
        clientsService.updateClient(client);
        verify(clientsRepository).save(client);
    }

    @Test
    void getClientByEmail(){
        clientsService.getClientByEmail("email");
        verify(clientsRepository).findByEmail("email");
    }

    @Test
    void createRole(){
        Role role = new Role();
        clientsService.createRole(role);
        verify(rolesRepository).save(role);
    }

    @Test
    void createCompte(){
        Client client = new Client();
        client.setNom("Picard");
        client.setPays("FR");
        client.setEmail("jeremy55200@hotmail.com");
        Compte compte = new Compte();
        given(comptesRepository.save(compte)).willReturn(compte);
        clientsService.createCompte(compte,client);
        verify(clientsRepository).save(client);
    }

    @Test
    void verifyIBANisGeneratedCreateCompte(){
        Client client = new Client();
        client.setNom("Picard");
        client.setPays("FR");
        client.setEmail("jeremy55200@hotmail.com");
        Compte compte = new Compte();
        given(comptesRepository.save(compte)).willReturn(compte);
        clientsService.createCompte(compte,client);
        assertThat(compte.getIBAN()).isNotNull();
        assertThat(compte.getIBAN()).startsWith("FR");
    }

    @Test
    void verifyClientNoCompteCreateCompte(){
        Client client = new Client();
        client.setNom("Picard");
        client.setPays("FR");
        client.setEmail("jeremy55200@hotmail.com");
        Compte compte = new Compte();
        client.setCompte(compte);
        given(comptesRepository.save(compte)).willReturn(compte);
        assertThatThrownBy(() -> clientsService.createCompte(compte,client))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Vous avez déjà un compte.");
    }

    @Test
    void checkUsernameloadByUserName(){
        Client client = new Client();
        client.setNom("Picard");
        client.setEmail("admin@admin.fr");
        client.setMot_de_passe("password");
        Role role = new Role();
        role.setNom("ROLE_ADMIN");
        client.getRoles().add(role);
        given(clientsRepository.findByEmail("admin@admin.fr")).willReturn(client);
        UserDetails userDetails =  clientsService.loadUserByUsername("admin@admin.fr");
        assertThat(userDetails.getUsername()).isEqualTo("admin@admin.fr");
    }

    @Test
    void checkRoleloadByUserName(){
        Client client = new Client();
        client.setNom("Picard");
        client.setEmail("admin@admin.fr");
        client.setMot_de_passe("password");
        Role role = new Role();
        role.setNom("ROLE_ADMIN");
        client.getRoles().add(role);
        given(clientsRepository.findByEmail("admin@admin.fr")).willReturn(client);
        UserDetails userDetails =  clientsService.loadUserByUsername("admin@admin.fr");
        assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).isTrue();
    }

    @Test
    void clientNotInDBloadUserByUsername(){
        given(clientsRepository.findByEmail("email")).willReturn(null);
        assertThatThrownBy(() -> clientsService.loadUserByUsername("email"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Cet utilisateur n'existe pas.");
    }






}