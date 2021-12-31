package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.entities.Client;
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

}
