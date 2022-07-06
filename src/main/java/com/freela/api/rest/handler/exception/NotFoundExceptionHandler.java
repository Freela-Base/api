package com.freela.api.rest.handler.exception;

import com.freela.api.model.ErrorDto;
import com.freela.exception.NotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {NotFoundException.class, ExceptionHandler.class})
public class NotFoundExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse<?>> {
	@Override
	public HttpResponse<?> handle(HttpRequest request, NotFoundException exception) {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setErrorMessage(exception.getMessage());
		errorDto.setSource(exception.getSource());
		errorDto.setErrorCode("404");
		return HttpResponse.notFound(errorDto);
	}
}
