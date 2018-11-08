package com.fundoonotes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fundoonotes.handler.UserHandler;
import com.fundoonotes.utility.JWToken;

@Configuration
public class RouterConfig {
	
	@Autowired
	JWToken jwToken;
	
	@Bean
	RouterFunction<ServerResponse> routerFunction(UserHandler userHandler){
		return RouterFunctions.route(RequestPredicates.POST("/user/registration"), userHandler::register)
				.andRoute(RequestPredicates.POST("/user/login"), userHandler::login)
				.andRoute(RequestPredicates.GET("/user/forgotpassword"), userHandler::forgotPassword)
				.andRoute(RequestPredicates.PUT("/user/changepassword/{token}"), userHandler::changePassword)
				.andRoute(RequestPredicates.PUT("/user/verifyaccount/{token}"), userHandler::verifyAccount);
//				.filter(new HandlerFilter(jwToken));
	}

}
