package br.com.fiap.ecotech.ecotech_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Ponto de entrada da aplicação Spring Boot.
 * Essa anotação habilita auto-configuração, varredura de componentes (controllers, services etc.)
 * dentro do pacote br.com.fiap.ecotech.ecotech_api e subpacotes.
 */
@SpringBootApplication
public class EcotechApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcotechApiApplication.class, args);
	}
}
