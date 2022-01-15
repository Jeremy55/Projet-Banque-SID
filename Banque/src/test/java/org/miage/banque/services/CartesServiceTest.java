package org.miage.banque.services;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.exceptions.CarteNotFoundException;
import org.miage.banque.repositories.CartesRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartesServiceTest {

    @Mock private CartesRepository cartesRepository;
    private CartesService cartesService;

    @BeforeEach
    void setUp() {
        cartesService = new CartesService(cartesRepository);
    }

    @Test
    void getAllCartes(){
        cartesService.getAllCartes();
        verify(cartesRepository).findAll();
    }

    @Test
    void getCarteByNumero(){
        cartesService.getCarteByNumero("123456789");
        verify(cartesRepository).findByNumero("123456789");
    }

    @Test
    void getCarte(){
        Carte carte = new Carte();
        carte.setId(1L);
        given(cartesRepository.findById(1L))
                .willReturn(java.util.Optional.of(carte));
        cartesService.getCarte(1L);
        verify(cartesRepository).findById(1L);
    }


    @Test
    void canAddCarte(){
        Carte carte = new Carte();
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");

        cartesService.createCarte(carte, compte);
        ArgumentCaptor<Carte> carteArgumentCaptor = ArgumentCaptor.forClass(Carte.class);

        verify(cartesRepository).save(carteArgumentCaptor.capture());

        Carte carteArgumentCaptorValue = carteArgumentCaptor.getValue();
        assertThat(carteArgumentCaptorValue.getCompte().getIBAN()).isEqualTo("FR761234567890123456789012345");
    }

    @Test
    void checkDeletedCarteIsDisabled(){
        Carte carte = new Carte();
        carte.setId(1L);
        cartesService.deleteCarte(carte);
        ArgumentCaptor<Carte> carteArgumentCaptor = ArgumentCaptor.forClass(Carte.class);
        verify(cartesRepository).save(carteArgumentCaptor.capture());
        assertThat(carteArgumentCaptor.getValue().isActive()).isFalse();
    }

    @Test
    void generatedCarteHasAllAttributes(){
        Carte carte = new Carte();
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");

        cartesService.createCarte(carte, compte);
        ArgumentCaptor<Carte> carteArgumentCaptor = ArgumentCaptor.forClass(Carte.class);

        verify(cartesRepository).save(carteArgumentCaptor.capture());

        Carte carteArgumentCaptorValue = carteArgumentCaptor.getValue();
        assertThat(carteArgumentCaptorValue.getNumero()).isNotNull();
        assertThat(carteArgumentCaptorValue.getCode()).isNotNull();
        assertThat(carteArgumentCaptorValue.getCryptogramme()).isNotNull();
        assertThat(carteArgumentCaptorValue.getExpiration()).isNotNull();
    }

    @Test
    void willThrowIfCarteNotExists(){
        assertThatThrownBy(() -> cartesService.getCarte(1L))
                .isInstanceOf(CarteNotFoundException.class)
                .hasMessageContaining("Carte non trouvée.");
    }

    @Test
    void expirationOfCarteIsIn2WeeksIfVirtual(){
        //Given
        Carte carte = new Carte();
        carte.setVirtuelle(true);
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");
        //When
        cartesService.createCarte(carte, compte);
        ArgumentCaptor<Carte> carteArgumentCaptor = ArgumentCaptor.forClass(Carte.class);
        verify(cartesRepository).save(carteArgumentCaptor.capture());
        Carte carteArgumentCaptorValue = carteArgumentCaptor.getValue();
        //Then
        long daysBetweenNowAndExpiration = ChronoUnit.DAYS.between(new Date().toInstant(), carteArgumentCaptorValue.getExpiration().toInstant());
        assertThat(daysBetweenNowAndExpiration).isLessThanOrEqualTo(15);
        assertThat(daysBetweenNowAndExpiration).isGreaterThanOrEqualTo(13);
    }

    @Test
    void expirationWhenRealCarteIs2years(){
        //Given
        Carte carte = new Carte();
        carte.setVirtuelle(false);
        Compte compte = new Compte();
        compte.setIBAN("FR761234567890123456789012345");
        //When
        cartesService.createCarte(carte, compte);
        ArgumentCaptor<Carte> carteArgumentCaptor = ArgumentCaptor.forClass(Carte.class);
        verify(cartesRepository).save(carteArgumentCaptor.capture());
        Carte carteArgumentCaptorValue = carteArgumentCaptor.getValue();
        //Then
        long daysBetweenNowAndExpiration = ChronoUnit.DAYS.between(new Date().toInstant(), carteArgumentCaptorValue.getExpiration().toInstant());
        assertThat(daysBetweenNowAndExpiration).isCloseTo(365*2, Percentage.withPercentage(5));
    }


}