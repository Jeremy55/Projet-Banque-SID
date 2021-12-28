package org.miage.banque.controllers;

import lombok.RequiredArgsConstructor;
import org.miage.banque.services.ComptesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comptes")
@RequiredArgsConstructor

public class ComptesController {
    private final ComptesService comptesService;
}
