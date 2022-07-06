package com.freela.api.rest.handler.exception;

import com.freela.api.model.ErrorDto;
import com.freela.api.utils.ExceptionHandlerUtils;
import com.freela.exception.ApiException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;

@Produces
@Singleton
@Requires(classes = {Exception.class, ExceptionHandler.class})
public class GeneralExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {
	private static final Logger log = LoggerFactory.getLogger(GeneralExceptionHandler.class);

	@Inject
	ExceptionHandlerUtils exceptionHandlerUtils;

	@Override
	public HttpResponse<ErrorDto> handle(HttpRequest request, Exception exception) {
		ErrorDto errorDto = new ErrorDto();

		if(exception instanceof ApiException) {
			errorDto.setErrorMessage(exception.getMessage());
			errorDto.setSource(((ApiException) exception).getSource());
			errorDto.setErrorCode("400");
			return HttpResponse.badRequest(errorDto);
		}

		if(exception instanceof PersistenceException) {
			Throwable cause = exceptionHandlerUtils.getFirstException(exception);
			if(cause != null && cause.getMessage() != null) {
				if(cause.getMessage().contains("duplicate key value violates unique constraint")) {
					errorDto.setErrorCode("499");
					errorDto.setErrorMessage("Item already created");
					return HttpResponse.badRequest(errorDto);
				}
			}
		}

		exceptionHandlerUtils.populateGenericError(errorDto, exception);
		return HttpResponse.serverError(errorDto);
	}
}
