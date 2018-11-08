/*package com.fundoonotes.exception;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
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
import reactor.util.Loggers;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public DefaultWebExceptionHandler(GlobalErrorAttributes errorAttributes,
			ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
		super(errorAttributes, new ResourceProperties(), new ErrorProperties(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
		Loggers.getLogger(this.getClass()).info(((String) errorPropertiesMap.get("message")));
		return ServerResponse.status((HttpStatus) errorPropertiesMap.get("status")).contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject(errorPropertiesMap.get("message")));
	}

}
*/