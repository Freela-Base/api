package com.freela.api.rest.handler.exception;

import com.freela.api.model.ErrorDto;
import com.freela.exception.ForbiddenException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {ForbiddenException.class, ExceptionHandler.class})
public class ForbiddenExceptionHandler implements ExceptionHandler<ForbiddenException, HttpResponse<?>> {
	@Override
	public HttpResponse<?> handle(HttpRequest request, ForbiddenException exception) {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setErrorMessage(exception.getMessage());
		errorDto.setSource(exception.getSource());
		errorDto.setErrorCode("403");
		return HttpResponse.status(HttpStatus.FORBIDDEN).body(errorDto);
	}
}
