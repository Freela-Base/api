package com.freela.exception;

public class InvalidParameterException extends ApiException {
	public InvalidParameterException() {
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(String message, Source source) {
		super(message, source);
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParameterException(String message, Throwable cause, Source source) {
		super(message, cause, source);
	}

	public InvalidParameterException(Throwable cause) {
		super(cause);
	}

	public InvalidParameterException(Throwable cause, Source source) {
		super(cause, source);
	}

	public InvalidParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Source source) {
		super(message, cause, enableSuppression, writableStackTrace, source);
	}
}
