package org.miage.banque.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.exceptions.CompteNotFoundException;
import org.miage.banque.repositories.ComptesRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComptesServiceTest {

    @Mock
    private ComptesRepository comptesRepository;
    private ComptesService comptesService;

    @BeforeEach
    void setUp() {
        comptesService = new ComptesService(comptesRepository);
    }

    @Test
    void getCompte(){
        Compte compte = new Compte();
        when(comptesRepository.findById(1L)).thenReturn(java.util.Optional.of(compte));
        Compte compte1 = comptesService.getCompte(1L);
        verify(comptesRepository).findById(1L);
    }

    @Test
    void noCompteGetCompte(){
        assertThatThrownBy(() -> comptesService.getCompte(1L))
                .hasMessage("Ce compte n'existe pas.")
                .isInstanceOf(CompteNotFoundException.class);
    }

    @Test
    void getAllComptes(){
        comptesService.getAllComptes();
        verify(comptesRepository).findAll();
    }

    @Test
    void credit(){
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");
        compte.setSolde(100);
        when(comptesRepository.findCompteByIBAN("FR761234567890123456789012345")).thenReturn(compte);
        comptesService.credit(compte.getIBAN(), 50);
        verify(comptesRepository).save(compte);
        assertThat(compte.getSolde()).isEqualTo(150);
    }

    @Test
    void compteInOtherBankCredit(){
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");
        compte.setSolde(100);
        when(comptesRepository.findCompteByIBAN("FR761234567890123456789012345")).thenReturn(null);
        comptesService.credit(compte.getIBAN(), 50);
        verify(comptesRepository,never()).save(compte);
    }

    @Test
    void carteBelongsToCompte(){
        Compte compte = new Compte();
        Carte carte = new Carte();
        compte.getCartes().add(carte);
        assertThat(comptesService.carteBelongsToCompte(compte, carte)).isTrue();
    }

    @Test
    void notCarteBelongsToCompte(){
        Compte compte = new Compte();
        Carte carte = new Carte();
        assertThat(comptesService.carteBelongsToCompte(compte, carte)).isFalse();
    }

    @Test
    void debit(){
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");
        compte.setSolde(100);
        when(comptesRepository.findCompteByIBAN("FR761234567890123456789012345")).thenReturn(compte);
        comptesService.debit(compte.getIBAN(), 50);
        verify(comptesRepository).save(compte);
        assertThat(compte.getSolde()).isEqualTo(50);
    }

}