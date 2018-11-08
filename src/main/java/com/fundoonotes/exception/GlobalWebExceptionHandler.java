package com.fundoonotes.exception;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // to give it a higher priority than the DefaultErrorWebExceptionHandler which
			// is registered at @Order(-1)
public class GlobalWebExceptionHandler extends AbstractErrorWebExceptionHandler {
	
	Logger logger = Loggers.getLogger(GlobalWebExceptionHandler.class);

	/**
	 * @param globalErrorAttributes to get the custom error response we set
	 * @param applicationContext
	 * @param serverCodecConfigurer to configure HTTP message reader and writer
	 *                              options relevant on the server side
	 */
	public GlobalWebExceptionHandler(GlobalErrorAttributes globalErrorAttributes,
			ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
		super(globalErrorAttributes, new ResourceProperties(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::getErrorResponse);
	}

	public Mono<ServerResponse> getErrorResponse(ServerRequest serverRequest) {
		Map<String, Object> errorPropertiesMap = getErrorAttributes(serverRequest, false);
		logger.info(((String) errorPropertiesMap.get("message")).toUpperCase());
		return ServerResponse.status((HttpStatus) errorPropertiesMap.get("status")).contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject(errorPropertiesMap.get("message")));
	}

}
