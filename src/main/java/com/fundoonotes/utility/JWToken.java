package com.fundoonotes.utility;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fundoonotes.exception.JwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import reactor.core.publisher.Mono;

public class JWToken {

	public String createJWT(String issuer, String email, String id) {
		//System.out.println("12 "+Thread.currentThread().getId());
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		JwtBuilder builder = Jwts.builder()
				.setId(id)
				.setSubject(email)
				.setIssuedAt(new Date())
				.setIssuer(issuer)
				.signWith(signatureAlgorithm, "passKey");
		//System.out.println("20 "+Thread.currentThread().getId());
		return builder.compact();
	}
	
	public Mono<Claims> verifyToken(String token) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey("passKey")
					.parseClaimsJws(token)
					.getBody();
			return Mono.just(claims);
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new JwtException(HttpStatus.UNAUTHORIZED, "Token verification error");
//			return Mono.error(new JwtException(HttpStatus.UNAUTHORIZED, "Token expired"));
		} catch (ExpiredJwtException e) {
			return Mono.error(new JwtException(HttpStatus.UNAUTHORIZED, "Token expired"));
		}
	}
	
}
