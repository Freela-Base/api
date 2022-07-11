package com.freela.api.rest.handler.exception;

import com.freela.api.dto.ErrorDto;
import com.freela.api.utils.ExceptionHandlerUtils;
import com.freela.exception.NotFoundException;
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
@Requires(classes = {NotFoundException.class, ExceptionHandler.class})
public class NotFoundExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse<?>> {
	public static final Logger log = LoggerFactory.getLogger(NotFoundExceptionHandler.class);

	@Inject
	ExceptionHandlerUtils exceptionHandlerUtils;

	@Override
	public HttpResponse<?> handle(HttpRequest request, NotFoundException exception) {
		log.info("handle: { requestPath: {}, exception: {} }", request.getPath(), exception.getMessage());
		ErrorDto errorDto = new ErrorDto();
		errorDto.setErrorMessage(exception.getMessage());
		errorDto.setSource(exception.getSource());
		errorDto.setErrorCode("404");
		errorDto.setSource(exceptionHandlerUtils.populateSourceResource(errorDto.getSource(), request));
		return HttpResponse.notFound(errorDto);
	}
}
