package com.freela.api.rest.handler.exception;

import com.freela.api.dto.ErrorDto;
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

@Produces
@Singleton
@Requires(classes = {Exception.class, ExceptionHandler.class})
public class GeneralExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {
	private static final Logger log = LoggerFactory.getLogger(GeneralExceptionHandler.class);

	@Inject
	ExceptionHandlerUtils exceptionHandlerUtils;

	@Override
	public HttpResponse<ErrorDto> handle(HttpRequest request, Exception exception) {
		log.info("handle: { requestPath: {}, exception: {} }", request.getPath(), exception.getMessage());
		ErrorDto errorDto = new ErrorDto();

		if(exception instanceof ApiException) {
			errorDto.setErrorMessage(exception.getMessage());
			errorDto.setSource(((ApiException) exception).getSource());
			errorDto.setErrorCode("400");
			errorDto.setSource(exceptionHandlerUtils.populateSourceResource(errorDto.getSource(), request));
			return HttpResponse.badRequest(errorDto);
		}

		exceptionHandlerUtils.populateGenericError(errorDto, exception);
		errorDto.setSource(exceptionHandlerUtils.populateSourceResource(errorDto.getSource(), request));
		return HttpResponse.serverError(errorDto);
	}
}
