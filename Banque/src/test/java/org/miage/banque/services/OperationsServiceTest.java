package org.miage.banque.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miage.banque.delegate.ConversionServiceDelegate;
import org.miage.banque.delegate.LocalisationServiceDelegate;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.exceptions.OperationNotFoundException;
import org.miage.banque.repositories.OperationsRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationsServiceTest {

    @Mock
    private OperationsRepository operationsRepository;
    @Mock
    private ComptesService comptesService;
    @Mock
    private ConversionServiceDelegate conversionServiceDelegate;
    @Mock
    private LocalisationServiceDelegate localisationServiceDelegate;
    @Mock
    private CartesService cartesService;

    private OperationsService operationsService;

    @BeforeEach
    void setUp() {
        operationsService = new OperationsService(operationsRepository, comptesService, conversionServiceDelegate, localisationServiceDelegate, cartesService);
    }

    @Test
    void getAll(){
        operationsService.getAll();
        verify(operationsRepository).findAll();
    }

    @Test
    void getById(){
        Operation operation = new Operation();
        when(operationsRepository.findById(1L)).thenReturn(java.util.Optional.of(operation));
        operationsService.getOperation(1L);
        verify(operationsRepository).findById(1L);
    }

    @Test
    void notFoundGetById(){
        assertThatThrownBy(()-> operationsService.getOperation(1L))
                .isInstanceOf(OperationNotFoundException.class)
                .hasMessage("Cette opération n'exsite pas.");
    }

    @Test
    void happyPathcreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        carte.setCompte(compte);
        operation.setCarte(carte);

        operationsService.create(operation);

        verify(comptesService).debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());
        verify(comptesService).credit(operation.getIBAN_debiteur(), operation.getMontant());
        verify(operationsRepository).save(operation);
    }

    @Test
    void exceptionPlafondReachedCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(900);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        Operation operation1 = new Operation();
        operation1.setMontant(100.0);
        operation1.setDate(new Date());
        carte.setOperations(Set.of(operation1));
        carte.setCompte(compte);
        operation.setCarte(carte);

        assertThatThrownBy( ()-> operationsService.create(operation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La carte a atteint son plafond pour le mois glissant en cours.");
    }

    @Test
    void exceptionOperationTooHighForPlafondCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(200);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(100.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        carte.setCompte(compte);
        operation.setCarte(carte);

        assertThatThrownBy( ()-> operationsService.create(operation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La carte a atteint son plafond pour le mois glissant en cours.");
    }

    @Test
    void disabledCartecreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(false);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        carte.setCompte(compte);
        operation.setCarte(carte);

        assertThatThrownBy( ()-> operationsService.create(operation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La carte n'est pas active");
    }

    @Test
    void montantTooHighForCompteCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(1000.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(1500.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        carte.setCompte(compte);
        operation.setCarte(carte);


        assertThatThrownBy( ()-> operationsService.create(operation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Le montant demandé est supérieur au solde du compte");
    }

    @Test
    void usingVirtualCarteIsExpiredAfterUseCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(true);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        carte.setCompte(compte);
        operation.setCarte(carte);

        operationsService.create(operation);

        verify(comptesService).debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());
        verify(comptesService).credit(operation.getIBAN_debiteur(), operation.getMontant());
        verify(cartesService).deleteCarte(operation.getCarte());
        verify(operationsRepository).save(operation);
    }

    @Test
    void localisationCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        operation.setLatitude("48.866667");
        operation.setLongitude("2.333333");
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(true);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");
        Client client = new Client();
        client.setPays("FR");

        carte.setCompte(compte);
        compte.setClient(client);
        operation.setCarte(carte);

        when(localisationServiceDelegate.callLocalisationService(operation.getLongitude(), operation.getLatitude())).thenReturn("FR");
        operationsService.create(operation);

        verify(comptesService).debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());
        verify(comptesService).credit(operation.getIBAN_debiteur(), operation.getMontant());
        verify(operationsRepository).save(operation);
    }

    @Test
    void localisationWrongCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        operation.setLatitude("48.866667");
        operation.setLongitude("2.333333");
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(true);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");
        Client client = new Client();
        client.setPays("FR");

        carte.setCompte(compte);
        compte.setClient(client);
        operation.setCarte(carte);

        when(localisationServiceDelegate.callLocalisationService(operation.getLongitude(), operation.getLatitude())).thenReturn("CN");

        assertThatThrownBy(() -> operationsService.create(operation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Vous ne pouvez pas utiliser cette carte dans un pays différent du votre" +
                        " car vous avez activé le système de protection basé sur la situation géographique.");
    }

    @Test
    void localisationWrongButNoLocalisatioOnCarteCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("EUR");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        operation.setLatitude("48.866667");
        operation.setLongitude("2.333333");
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");
        Client client = new Client();
        client.setPays("FR");

        carte.setCompte(compte);
        compte.setClient(client);
        operation.setCarte(carte);

        operationsService.create(operation);

        verify(comptesService).debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());
        verify(comptesService).credit(operation.getIBAN_debiteur(), operation.getMontant());
        verify(operationsRepository).save(operation);
    }

    @Test
    void deviseDifferentThanUserCreate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Operation operation = new Operation();
        operation.setDevise("USD");
        operation.setMontant(100.0);
        operation.setIBAN_debiteur("FR7612345678901234567890123456789");
        operation.setCategorie("Achat");
        operation.setDate(new Date());
        Carte carte = new Carte();
        carte.setActive(true);
        carte.setExpiration(calendar.getTime());
        carte.setNumero("123456789");
        carte.setLocalisation(false);
        carte.setVirtuelle(false);
        carte.setPlafond(1000.0);
        Compte compte = new Compte();
        compte.setDevise("EUR");
        compte.setSolde(1000.0);
        compte.setIBAN("FR761234567890123456789012345");

        carte.setCompte(compte);
        operation.setCarte(carte);
        when(conversionServiceDelegate.callConversionService(operation.getDevise(),operation.getCarte().getCompte().getDevise(),operation.getMontant())).thenReturn(88.0);
        operationsService.create(operation);
        verify(comptesService).debit(operation.getCarte().getCompte().getIBAN(), operation.getMontant());
        verify(comptesService).credit(operation.getIBAN_debiteur(), operation.getMontant());
        verify(operationsRepository).save(operation);
        assertThat(operation.getMontant()).isEqualTo(88.0);
        assertThat(operation.getMontant_avant_conversion()).isEqualTo(100.0);
    }



}