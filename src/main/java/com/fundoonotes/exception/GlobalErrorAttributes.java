package com.fundoonotes.exception;

import java.util.Map;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	public GlobalErrorAttributes() {
		super(true); //true: includes exception type in ServerRequest
	}
	
	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
		Throwable error = getError(request);
		errorAttributes.put("status", getHttpStatus(error));
		errorAttributes.put("message", getReason(error));
		return errorAttributes;
	}
	
	public HttpStatus getHttpStatus(Throwable error) {
		if(error instanceof ResponseStatusException)
			return ((ResponseStatusException) error).getStatus();
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public String getReason(Throwable error) {
		if(error instanceof ResponseStatusException)
			return ((ResponseStatusException) error).getReason();
		return error.getMessage();
	}
	
}
