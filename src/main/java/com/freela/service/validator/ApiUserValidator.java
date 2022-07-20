package com.freela.service.validator;

import com.freela.database.model.ApiUser;
import com.freela.exception.ApiException;
import com.freela.exception.ForbiddenException;
import com.freela.exception.InvalidParameterException;
import com.freela.exception.NotFoundException;
import com.freela.service.parameter.ApiUserSearchRequest;
import com.freela.utils.PasswordUtils;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Singleton
public class ApiUserValidator {
	private static final Logger log = LoggerFactory.getLogger(ApiUserValidator.class);

	@Inject
	PasswordUtils passwordUtils;

	public void validatePassword(String password) {
		//TODO add more complex validations for password
		// i.e. Upper Case and Lower Case letter, Number, Special Character (create regex?)
		if (password == null || password.length() < 8) {
			throw new InvalidParameterException("Invalid Password", new ApiException.Source(
					ApiException.Location.BODY,
					"password",
					"***",
					"Min length: 8"));
		}
	}

	public void validateUser(ApiUser apiUser, ApiException.Source source) {
		if (apiUser == null) {
			throw new NotFoundException("User not found", source);
		}
	}

	public void checkPassword(ApiUser apiUser, String password) {
		try {
			if (passwordUtils.isValidPassword(apiUser, password)) {
				return;
			}
		} catch (Exception e) {
			log.error("checkPassword: error while checking password", e);
		}
		// If not valid or exception happens throws error
		throw new ForbiddenException("Invalid email or password");
	}

	public void isNullOrNotValidated(ApiUser apiUser, ApiException.Source source) {
		if(apiUser == null || !apiUser.isValidated()) {
			return;
		}

		throw new InvalidParameterException("User already registered", source);
	}

	public void validateRoleIds(Collection<Long> roleIds) {
		if (CollectionUtils.isEmpty(roleIds)) {
			throw new InvalidParameterException("Role IDs cannot be empty or null", new ApiException.Source(
					ApiException.Location.BODY,
					"roleIds",
					"%s".formatted(roleIds),
					"Valid list of Role IDs"));
		}
	}

	public void validateSearch(ApiUserSearchRequest contactSearchRequest) {
		if(contactSearchRequest == null
				|| (StringUtils.isEmpty(contactSearchRequest.getEmail())
				&& StringUtils.isEmpty(contactSearchRequest.getName()))
		) {
			throw new InvalidParameterException("Empty search criteria", new ApiException.Source(
					ApiException.Location.QUERY,
					"[email, name]",
					"Empty or null",
					"Valid search criteria"
			));
		}

		if(contactSearchRequest.getEmail() == null) {
			contactSearchRequest.setEmail(contactSearchRequest.getName());
		} else if(contactSearchRequest.getName() == null) {
			contactSearchRequest.setName(contactSearchRequest.getEmail());
		}
	}

	public void validateUserToSave(
			@NonNull ApiUser newApiUser,
			@Nullable ApiUser savedApiUser,
			String password
	) {
		// Validate if there is one user already saved
		isNullOrNotValidated(savedApiUser, new ApiException.Source(
				ApiException.Location.BODY,
				"email",
				newApiUser.getEmail(),
				"Unregistered email"
		));
		if(StringUtils.isNotEmpty(password)) {
			validatePassword(password);
		}
	}
}
