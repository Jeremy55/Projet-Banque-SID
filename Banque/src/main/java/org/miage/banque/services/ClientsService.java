package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.compte.CompteUtils;
import org.miage.banque.exceptions.ClientNotFoundException;
import org.miage.banque.exceptions.InvalidTokenException;
import org.miage.banque.repositories.ClientsRepository;
import org.miage.banque.repositories.ComptesRepository;
import org.miage.banque.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientsService implements UserDetailsService {

    private final ClientsRepository clientsRepository;
    private final RolesRepository rolesRepository;
    private final ComptesRepository comptesRepository;

    private final PasswordEncoder passwordEncoder;

    public Client getClient(Long id){
        log.info("Récupération du client avec l'id {}", id);
        return clientsRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Ce client n'existe pas."));
    }

    public Iterable<? extends Client> getAllClients() {
        log.info("Récupération de tous les clients");
        return clientsRepository.findAll();
    }

    public Client createClient(Client client) {
        log.info("Création d'un nouveau client {}", client.getNom());
        client.setMot_de_passe(passwordEncoder.encode(client.getMot_de_passe()));
        return clientsRepository.save(client);
    }

    public Client updateClient(Client clientToUpdate) {
        log.info("Mise à jour du client {}", clientToUpdate.getNom());
        return clientsRepository.save(clientToUpdate);
    }

    public Client getClientByEmail(String email) {
        log.info("Récupération du client avec l'email {}", email);
        return clientsRepository.findByEmail(email);
    }

    public void createRole(Role role) {
        log.info("Création du role {}", role.getNom());
        rolesRepository.save(role);
    }

    public void addRoleToClient(String email, String roleName) {
        log.info("Ajout du rôle {} au client {}", roleName, email);
        Client client = clientsRepository.findByEmail(email);
        Role role = rolesRepository.findByNom(roleName);
        client.getRoles().add(role);
    }

    public Compte createCompte(Compte compte, Client client) {
        log.info("Création d'un compte pour le client {}", client.getNom());
        compte.setIBAN(CompteUtils.randomIban(client.getPays()));
        compte =  comptesRepository.save(compte);
        log.info("Ajout du compte {} au client {}", compte.getIBAN(), client.getEmail());
        if(client.getCompte() != null) {
            throw new RuntimeException("Vous avez déjà un compte.");
        }
        client.setCompte(compte);
        clientsRepository.save(client);
        return compte;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Client client = clientsRepository.findByEmail(mail);
        if (client == null) {
            log.error("Client avec le mail {} n'existe pas. ", mail);
            throw new UsernameNotFoundException("User not found");
        }
        log.info("Utilisateur avec le mail {} trouvé.",mail);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        client.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getNom()));
        });
        return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getMot_de_passe(), authorities);
    }

    public void deleteAll() {
        clientsRepository.deleteAll();
        comptesRepository.deleteAll();
        rolesRepository.deleteAll();
    }
}
