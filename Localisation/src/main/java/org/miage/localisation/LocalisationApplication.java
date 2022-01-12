package org.miage.localisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication
@EnableDiscoveryClient
public class LocalisationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalisationApplication.class, args);
    }
}
