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
}
