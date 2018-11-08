package com.fundoonotes.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fundoonotes.model.User;
import com.fundoonotes.service.UserService;

import reactor.core.publisher.Mono;

@Configuration
public class UserHandler {
	
	@Autowired
	private UserService userService;
	
	public Mono<ServerResponse> register(ServerRequest serverRequest){
		Mono<User> monoUser = serverRequest.bodyToMono(User.class);
//		Mono<String> res = userService.register(monoUser);
//		return ServerResponse.ok().body(userService.register(monoUser), String.class);
		return userService.register(monoUser)
					.flatMap(s -> ServerResponse
							.ok()
							.contentType(MediaType.TEXT_PLAIN)
							.syncBody(s));
	}
	
	public Mono<ServerResponse> login(ServerRequest serverRequest){
		Mono<User> monoUser = serverRequest.bodyToMono(User.class);
//		System.out.println("40 "+Thread.currentThread().getId());
		return userService.login(monoUser)
				.flatMap(s -> {
					return ServerResponse
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.syncBody(s);});
	}
	
	public Mono<ServerResponse> verifyAccount(ServerRequest serverRequest){
		String token = serverRequest.pathVariable("token");
		return userService.verifyAccount(token)
				.flatMap(s -> ServerResponse
						.ok()
						.contentType(MediaType.TEXT_PLAIN)
						.syncBody(s));
	}
	
	public Mono<ServerResponse> forgotPassword(ServerRequest serverRequest){
		String email = serverRequest.queryParam("email").get();
		return userService.forgotPassword(email)
				.flatMap(s -> ServerResponse
						.ok()
						.contentType(MediaType.TEXT_PLAIN)
						.syncBody(s));
	}
	
	public Mono<ServerResponse> changePassword(ServerRequest serverRequest){
		String password = serverRequest.queryParam("password").get();
		String token = serverRequest.pathVariable("token");
//		System.out.println("Valid? "+ serverRequest.attribute("valid").get());
		return userService.changePassword(token, password)
				.flatMap(s -> ServerResponse
						.ok()
						.contentType(MediaType.TEXT_PLAIN)
						.syncBody(s));
	}
	
	public Mono<ServerResponse> options(ServerRequest serverRequest){
		return ServerResponse.ok().syncBody("");
	}

}
