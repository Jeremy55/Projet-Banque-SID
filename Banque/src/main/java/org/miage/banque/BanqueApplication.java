package org.miage.banque;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.delegate.ConversionServiceDelegate;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.carte.Carte;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.entities.operation.Operation;
import org.miage.banque.services.CartesService;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.miage.banque.services.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class BanqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanqueApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	// http://127.0.0.1:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
	@Bean
	public OpenAPI banqueAPI() {
		return new OpenAPI().info(new Info()
				.title("Banque API")
				.version("1.0")
				.description("Documentation sommaire de API Banque 1.0"));
	}

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}


	@Bean
	@Profile("!test")
	CommandLineRunner run(ClientsService clientsService, CartesService cartesService, ComptesService comptesService, OperationsService operationsService) {
		return args -> {
			log.info("Création des données de test pour la base de données.");

			//conversionServiceDelegate.callStudentServiceAndGetData("EUR","USD",1000);

			log.info("Création des roles.");
			clientsService.createRole(new Role(null,"ROLE_CLIENT"));
			clientsService.createRole(new Role(null,"ROLE_ADMIN"));

			log.info("Création des clients normaux.");
			Client client1 = new Client();
			client1.setNom("Picard");
			client1.setPrenom("Jérémy");
			client1.setPays("FR");
			client1.setNo_passeport("123456789");
			client1.setTelephone("0329916847");
			client1.setEmail("jeremy55200@hotmail.fr");
			client1.setMot_de_passe("azerty123");
			clientsService.createClient(client1);
			clientsService.addRoleToClient("jeremy55200@hotmail.fr","ROLE_CLIENT");

			Client client2 = new Client();
			client2.setNom("Shuai");
			client2.setPrenom("Vitoria");
			client2.setPays("FR");
			client2.setNo_passeport("578456789");
			client2.setTelephone("0729116847");
			client2.setEmail("vitoria@qqmail.cn");
			client2.setMot_de_passe("cc123");
			clientsService.createClient(client2);
			clientsService.addRoleToClient("vitoria@qqmail.cn","ROLE_CLIENT");

			log.info("Création des comptes.");

			Compte compte = new Compte();
			compte.setSolde(1000);
			compte.setDevise("EUR");
			clientsService.createCompte(compte,client1);

			Compte compte2 = new Compte();
			compte2.setSolde(1000);
			compte2.setDevise("EUR");
			clientsService.createCompte(compte2,client2);

			log.info("Création des cartes");
			Carte carte = new Carte();
			carte.setActive(true);
			carte.setLocalisation(false);
			carte.setVirtuelle(false);
			carte.setContact(true);
			carte.setPlafond(1000);
			carte = cartesService.createCarte(carte,client1.getCompte());

			Operation operation = new Operation();
			operation.setCategorie("Loisir");
			operation.setIBAN_debiteur(client2.getCompte().getIBAN());
			operation.setDevise("EUR");
			operation.setMontant(150);
			operation.setCarte(carte);
			operationsService.create(operation);

			log.info("Création de l'admin.");
			Client client3 = new Client();
			client3.setNom("Admin");
			client3.setPrenom("Admin");
			client3.setPays("France");
			client3.setNo_passeport("123456799");
			client3.setTelephone("0329917847");
			client3.setEmail("admin@admin.fr");
			client3.setMot_de_passe("root");
			clientsService.createClient(client3);
			clientsService.addRoleToClient("admin@admin.fr","ROLE_ADMIN");
			clientsService.addRoleToClient("admin@admin.fr","ROLE_CLIENT");
		};
	}
}
