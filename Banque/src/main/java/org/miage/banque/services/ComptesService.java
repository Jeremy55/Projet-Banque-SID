package org.miage.banque.services;

import lombok.RequiredArgsConstructor;
import org.miage.banque.repositories.ComptesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComptesService {
    private final ComptesRepository comptesRepository;
}
