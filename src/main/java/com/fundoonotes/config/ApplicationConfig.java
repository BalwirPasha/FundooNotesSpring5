package com.fundoonotes.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fundoonotes.model.MyMail;
import com.fundoonotes.utility.JWToken;
import com.fundoonotes.utility.MailSender;

import reactor.core.publisher.TopicProcessor;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public MailSender email() {
		return new MailSender(userRegistration());
	}
	
	@Bean
	public JWToken jwToken() {
		return new JWToken();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public TopicProcessor<MyMail> userRegistration() {
		return TopicProcessor.<MyMail>create();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
