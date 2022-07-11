package com.freela.api.dto;

import com.freela.exception.ApiException;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class ErrorDto {
	private String errorMessage;
	private String errorCode;
	private ApiException.Source source;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public ApiException.Source getSource() {
		return source;
	}

	public void setSource(ApiException.Source source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "ErrorDto{" +
				"errorMessage='" + errorMessage + '\'' +
				", errorCode='" + errorCode + '\'' +
				", source=" + source +
				'}';
	}
}
