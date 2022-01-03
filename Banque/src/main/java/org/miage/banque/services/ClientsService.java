package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.client.Client;
import org.miage.banque.exceptions.ClientNotFoundException;
import org.miage.banque.repositories.ClientsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientsService {
    private final ClientsRepository clientsRepository;

    public Client getClient(Long id){
        return clientsRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Ce client n'existe pas."));
    }

    public Iterable<? extends Client> getAllClients() {
        return clientsRepository.findAll();
    }

    public Client createClient(Client client) {
        return clientsRepository.save(client);
    }

    public Client updateClient(Client clientToUpdate) {
        return clientsRepository.save(clientToUpdate);
    }

    public void deleteClient(Long clientId) {
        clientsRepository.deleteById(clientId);
    }
}
