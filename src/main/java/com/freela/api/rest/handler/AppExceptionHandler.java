package com.freela.api.rest.handler;

import com.freela.api.model.ErrorDto;
import com.freela.exception.ApiException;
import com.freela.exception.ForbiddenException;
import com.freela.exception.NotFoundException;
import com.freela.utils.HandlerUtils;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
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
public class AppExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {
	private static final Logger log = LoggerFactory.getLogger(AppExceptionHandler.class);

	@Inject
	HandlerUtils handlerUtils;

	@Override
	public HttpResponse<ErrorDto> handle(HttpRequest request, Exception exception) {
		ErrorDto errorDTO = new ErrorDto();

		if(exception instanceof ApiException) {
			errorDTO.setErrorMessage(exception.getMessage());
			errorDTO.setSource(((ApiException) exception).getSource());

			if(exception instanceof NotFoundException) {
				errorDTO.setErrorCode("404");
				return HttpResponse.notFound(errorDTO);
			}

			if(exception instanceof ForbiddenException) {
				errorDTO.setErrorCode("403");
				return HttpResponse.status(HttpStatus.FORBIDDEN).body(errorDTO);
			}

			errorDTO.setErrorCode("400");
			return HttpResponse.badRequest(errorDTO);
		}

		if(exception instanceof PersistenceException) {
			Throwable cause = handlerUtils.getFirstException(exception);
			if(cause != null && cause.getMessage() != null) {
				if(cause.getMessage().contains("duplicate key value violates unique constraint")) {
					errorDTO.setErrorCode("499");
					errorDTO.setErrorMessage("Item already created");
					return HttpResponse.badRequest(errorDTO);
				}
			}
		}

		log.error("Unexpected Exception", exception);

		errorDTO.setErrorCode("500");
		errorDTO.setErrorMessage("Unexpected Exception");
		return HttpResponse.serverError(errorDTO);
	}
}
