package com.freela.service.validator;

import com.freela.exception.ApiException;
import com.freela.exception.InvalidParameterException;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

@Singleton
public class FieldValidator {
	public void validateString(String string, ApiException.Source source) {
		if (StringUtils.isEmpty(string)) {
			throw new InvalidParameterException("Invalid Parameter", source);
		}
	}
}
