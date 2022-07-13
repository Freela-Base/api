package com.freela.api.rest.handler.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.freela.api.dto.ErrorDto;
import com.freela.api.utils.ExceptionHandlerUtils;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
@Requires(classes = {JsonProcessingException.class, ExceptionHandler.class})
@Replaces(bean = io.micronaut.http.server.exceptions.JsonExceptionHandler.class)
public class JsonExceptionHandler  implements ExceptionHandler<JsonProcessingException, HttpResponse<ErrorDto>> {

	private static final Logger log = LoggerFactory.getLogger(JsonExceptionHandler.class);

	@Inject
	ExceptionHandlerUtils exceptionHandlerUtils;

	@Override
	public HttpResponse<ErrorDto> handle(HttpRequest request, JsonProcessingException exception) {
		log.info("handle: { requestPath: {}, exception: {} }", request.getPath(), exception.getMessage());
		ErrorDto errorDto = new ErrorDto();
		errorDto.setErrorMessage(exception.getMessage());
		errorDto.setErrorCode("400");
		errorDto.setSource(exceptionHandlerUtils.populateSourceResource(errorDto.getSource(), request));
		return HttpResponse.badRequest(errorDto);
	}
}
