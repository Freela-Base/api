package com.freela.api.utils;

import com.freela.api.model.ErrorDto;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ExceptionHandlerUtils {
	private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerUtils.class);

	public void populateGenericError(
			@NonNull ErrorDto errorDto,
			@NonNull Exception exception
	) {
		log.error("Unexpected Exception", exception);
		errorDto.setErrorCode("500");
		errorDto.setErrorMessage("Unexpected Exception");
	}

	public Throwable getFirstException(Throwable throwable) {
		if(throwable == null)
			return  null;

		while(throwable.getCause() != null)
			throwable = throwable.getCause();

		return throwable;
	}
}
