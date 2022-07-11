package com.freela.service.validator;

import com.freela.database.model.ApiUser;
import com.freela.database.model.RefreshToken;
import com.freela.exception.ApiException;
import com.freela.exception.InvalidParameterException;
import jakarta.inject.Singleton;

import java.util.Locale;

@Singleton
public class RefreshTokenValidator {

	private static boolean isApiUserInvalid(ApiUser apiUser) {
		return apiUser == null
				|| apiUser.getId() == null
				|| Boolean.FALSE.equals(apiUser.isValidated())
				|| Boolean.TRUE.equals(apiUser.isDeleted());
	}

	public void validateNew(RefreshToken refreshToken) {
		if(refreshToken == null) {
			throw new InvalidParameterException("RefreshToken can't be null", new ApiException.Source(
					ApiException.Location.BODY,
					"Refresh Token",
					"null",
					"Valid Refresh Token"));
		}

		if(isApiUserInvalid(refreshToken.getApiUser())) {
			throw new InvalidParameterException("Invalid User");
		}

		if(refreshToken.getDevice() == null
				|| refreshToken.getDevice().getId() == null) {
			throw new InvalidParameterException("Invalid Device");
		}
	}
}
