package com.fundoonotes.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fundoonotes.model.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRespository extends ReactiveMongoRepository<User, String> {
	
	public Mono<User> findByEmail(String emailId);
	
	public Mono<User> findByEmailAndPassword(String emailId, String Password);

}
