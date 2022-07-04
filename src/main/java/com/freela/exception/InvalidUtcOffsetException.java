package com.freela.exception;

public class InvalidUtcOffsetException extends ApiException {
	public InvalidUtcOffsetException() {
	}

	public InvalidUtcOffsetException(String message) {
		super(message);
	}

	public InvalidUtcOffsetException(String message, Source source) {
		super(message, source);
	}

	public InvalidUtcOffsetException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUtcOffsetException(String message, Throwable cause, Source source) {
		super(message, cause, source);
	}

	public InvalidUtcOffsetException(Throwable cause) {
		super(cause);
	}

	public InvalidUtcOffsetException(Throwable cause, Source source) {
		super(cause, source);
	}

	public InvalidUtcOffsetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidUtcOffsetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Source source) {
		super(message, cause, enableSuppression, writableStackTrace, source);
	}
}
