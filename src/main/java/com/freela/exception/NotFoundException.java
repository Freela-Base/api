package com.freela.exception;

public class NotFoundException extends ApiException {
	public NotFoundException() {
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Source source) {
		super(message, source);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(String message, Throwable cause, Source source) {
		super(message, cause, source);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(Throwable cause, Source source) {
		super(cause, source);
	}

	public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Source source) {
		super(message, cause, enableSuppression, writableStackTrace, source);
	}
}
