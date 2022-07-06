package com.freela.api.rest.handler.exception;

import com.freela.api.model.ErrorDto;
import com.freela.api.utils.ExceptionHandlerUtils;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.persistence.PersistenceException;

@Produces
@Singleton
@Requires(classes = {PersistenceException.class, ExceptionHandler.class})
public class PersistenceExceptionHandler implements ExceptionHandler<PersistenceException, HttpResponse<?>> {
	@Inject
	ExceptionHandlerUtils exceptionHandlerUtils;

	@Override
	public HttpResponse<?> handle(HttpRequest request, PersistenceException exception) {
		ErrorDto errorDto = new ErrorDto();
		Throwable cause = exceptionHandlerUtils.getFirstException(exception);
		if(cause != null && cause.getMessage() != null) {
			if(cause.getMessage().contains("duplicate key value violates unique constraint")) {
				errorDto.setErrorCode("499");
				errorDto.setErrorMessage("Item already created");
				return HttpResponse.badRequest(errorDto);
			}
		}

		exceptionHandlerUtils.populateGenericError(errorDto, exception);
		return HttpResponse.serverError(errorDto);
	}
}
