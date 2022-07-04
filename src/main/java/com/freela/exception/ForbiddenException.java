package com.freela.exception;

public class ForbiddenException extends ApiException {
	public ForbiddenException() {
	}

	public ForbiddenException(String message) {
		super(message);
	}

	public ForbiddenException(String message, Source source) {
		super(message, source);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForbiddenException(String message, Throwable cause, Source source) {
		super(message, cause, source);
	}

	public ForbiddenException(Throwable cause) {
		super(cause);
	}

	public ForbiddenException(Throwable cause, Source source) {
		super(cause, source);
	}

	public ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Source source) {
		super(message, cause, enableSuppression, writableStackTrace, source);
	}
}
