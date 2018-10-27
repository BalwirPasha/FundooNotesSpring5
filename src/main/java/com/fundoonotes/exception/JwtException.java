package com.fundoonotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JwtException extends ResponseStatusException {

	private static final long serialVersionUID = 5678929201424355015L;

	public JwtException(HttpStatus status, String reason) {
		super(status, reason);
	}

}
