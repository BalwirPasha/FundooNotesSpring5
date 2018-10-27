package com.fundoonotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserException extends ResponseStatusException {

	private static final long serialVersionUID = 2921869445225423945L;

	public UserException(HttpStatus status, String reason) {
		super(status, reason);
	}
	
}
