package com.fundoonotes.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpStatus.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fundoonotes.exception.UserException;
import com.fundoonotes.model.MyMail;
import com.fundoonotes.model.User;
import com.fundoonotes.model.UserResponse;
import com.fundoonotes.repository.UserRespository;
import com.fundoonotes.utility.JWToken;

import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;
import reactor.util.Logger;
import reactor.util.Loggers;

@Configuration
public class UserService {
	
	Logger LOGGER = Loggers.getLogger(UserService.class);
	
	@Autowired
	private UserRespository userRespository;
	
	@Autowired
	private JWToken jwToken;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private TopicProcessor<MyMail> userRegistration;
	
	public Mono<String> register(Mono<User> monoUser) {
		System.out.println("Ser "+Thread.currentThread().getId());
		monoUser = monoUser.map(user -> {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			return user;
		});
		return monoUser.flatMap(user -> userRespository.findByEmail(user.getEmail())
				.flatMap(foundUser -> Mono.<String>error(new UserException(BAD_REQUEST, "Email already registered")))
				.switchIfEmpty(userRespository.save(user)
						/*
						 * Extra test, just to check exception handling, can be removed(wont affect the
						 * service)
						 */
						.onErrorResume(e -> Mono.error(new Exception("Cannot save to dB")))  
						.flatMap(reguser -> {
							MyMail myMail = new MyMail();
							myMail.setTo(reguser.getEmail());
							myMail.setSubject("Link to activate");
							myMail.setText("Click here to activate your account.");
							userRegistration.onNext(myMail);
							return Mono.just("Registration Success");
					}))
				);
	}
	
	public Mono<UserResponse> login(Mono<User> monoUser){
		return monoUser.flatMap(user -> userRespository.findByEmail(user.getEmail())
				.flatMap(foundUser -> {
					if(bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword()))
						return Mono.just(foundUser);
					return Mono.error(new UserException(UNAUTHORIZED, "Incorrect Password"));
				})
				.flatMap(foundUser -> {
					if(foundUser.isVerified()) {
						UserResponse ur = modelMapper.map(foundUser, UserResponse.class);
						ur.setToken(jwToken.createJWT("Admin", foundUser.getEmail(), foundUser.getId()));
						return Mono.just(ur);
					}
					return Mono.error(new UserException(UNAUTHORIZED, "Please Activate Account First"));
				})
				.switchIfEmpty(Mono.error(new UserException(NOT_FOUND, "Invalid Email Id"))));
	}
	
	public Mono<String> forgotPassword(String email){
		return userRespository.findByEmail(email)
				.flatMap(user -> {
					MyMail myMail = new MyMail();
					myMail.setTo(user.getEmail());
					myMail.setSubject("Password reset link");
					myMail.setText("Click here to change your password.");
					userRegistration.onNext(myMail);
					return Mono.just("Mail sent, with password reset link");
				}).switchIfEmpty(Mono.error(new UserException(NOT_FOUND, "Incorrect email id")));
	}
	
	public Mono<String> changePassword(String token, String password){
		return jwToken.verifyToken(token)
//				.onErrorResume(e -> Mono.error(e))
				.map(claims -> claims.getSubject())
				.flatMap(email -> userRespository.findByEmail(email)
						.flatMap(user -> {
							user.setPassword(bCryptPasswordEncoder.encode(password));
							return userRespository.save(user)
									.map(us -> "Password changed successfully");
						})
						.switchIfEmpty(Mono.error(new UserException(NOT_FOUND, "User not found"))));
				
	}
	
	public Mono<String> verifyAccount(String token){
		return jwToken.verifyToken(token)
//			.onErrorResume(e -> Mono.error(e))
			.map(claims -> claims.getSubject())
			.flatMap(email -> userRespository.findByEmail(email)
				.flatMap(user -> {
					if(user.isVerified())
						return Mono.error(new UserException(BAD_REQUEST, "You are already verified"));
					user.setVerified(true);
					return userRespository.save(user)
						.map(us -> "Account verfied");
					})
				.switchIfEmpty(Mono.error(new UserException(NOT_FOUND, "User not found"))));
	}

}
