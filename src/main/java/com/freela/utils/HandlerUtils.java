package com.freela.utils;

import jakarta.inject.Singleton;

@Singleton
public class HandlerUtils {
	public Throwable getFirstException(Throwable throwable) {
		if(throwable == null)
			return  null;

		while(throwable.getCause() != null)
			throwable = throwable.getCause();

		return throwable;
	}
}
