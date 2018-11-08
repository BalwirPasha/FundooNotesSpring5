package com.fundoonotes.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fundoonotes.exception.JwtException;
import com.fundoonotes.utility.JWToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import reactor.core.publisher.Mono;

@Component
public class HandlerFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
	
	private JWToken jwToken;
	
	public HandlerFilter(JWToken jwToken) {
		this.jwToken = jwToken;
	}	
	
	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> handlerFunction) {
		if(true) {
			System.out.println(request.method() +"  "+request.methodName());
		}
		String token = null;
		try {
			token = request.headers().header("Authorization").get(0);
		}catch (IndexOutOfBoundsException e) {
			return Mono.error(new JwtException(HttpStatus.UNAUTHORIZED, "token missing"));
		}
		return jwToken.verifyToken(token)
				.flatMap(claims -> {
					request.attributes().put("valid", claims.getId());
					return handlerFunction.handle(request);
				})
				.switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED)
						.contentType(MediaType.TEXT_PLAIN)
						.syncBody("Unauthorized"));
	}
	
	public String verifyForFilter(String token) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey("passKey")
					.parseClaimsJws(token)
					.getBody();
			return claims.getSubject();
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new JwtException(HttpStatus.UNAUTHORIZED, "Token verification error");
		} catch (ExpiredJwtException e) {
			throw new JwtException(HttpStatus.UNAUTHORIZED, "Token Expired");
		}
	}

}
