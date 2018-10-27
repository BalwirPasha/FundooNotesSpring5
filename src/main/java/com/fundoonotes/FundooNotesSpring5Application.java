package com.fundoonotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class FundooNotesSpring5Application {

	public static void main(String[] args) {
		SpringApplication.run(FundooNotesSpring5Application.class, args);
	}
	
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		return httpSecurity.csrf().disable()
				.authorizeExchange().anyExchange().permitAll()
				.and().build();
	}
}
