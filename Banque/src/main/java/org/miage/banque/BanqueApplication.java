package org.miage.banque;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.entities.compte.Compte;
import org.miage.banque.services.ClientsService;
import org.miage.banque.services.ComptesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
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

	@Bean
	CommandLineRunner run(ClientsService clientsService, ComptesService comptesService) {
		return args -> {
			clientsService.createRole(new Role(null,"ROLE_CLIENT"));
			clientsService.createRole(new Role(null,"ROLE_ADMIN"));

			Client client1 = new Client();
			client1.setNom("Picard");
			client1.setPrenom("Jérémy");
			client1.setPays("France");
			client1.setNo_passeport("123456789");
			client1.setTelephone("0329916847");
			client1.setEmail("jeremy55200@hotmail.fr");
			client1.setMot_de_passe("azerty123");
			Client clientSaved = clientsService.createClient(client1);
			clientsService.addRoleToClient("jeremy55200@hotmail.fr","ROLE_CLIENT");
			System.out.println(clientSaved.getRoles());
		};
	}
}
