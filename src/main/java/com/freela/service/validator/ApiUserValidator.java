package com.freela.service.validator;

import com.freela.database.model.ApiUser;
import com.freela.exception.ApiException;
import com.freela.exception.ForbiddenException;
import com.freela.exception.InvalidParameterException;
import com.freela.exception.NotFoundException;
import com.freela.service.constants.ApiExceptionConstants;
import com.freela.service.parameter.ApiUserSearchRequest;
import com.freela.utils.PasswordUtils;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class ApiUserValidator {
	private static final Logger log = LoggerFactory.getLogger(ApiUserValidator.class);
	@Value("${com.freela.service.authentication.password-regex}")
	private String PASSWORD_PATTERN;
	private Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	@Inject
	PasswordUtils passwordUtils;

	public void validatePassword(String password) {
		if (password == null) {
			throw new InvalidParameterException("Invalid Password", ApiExceptionConstants.EMPTY_OR_NULL_PASSWORD);
		}
		Matcher matcher = pattern.matcher(password);
		if (!matcher.matches()) {
			throw new InvalidParameterException("Invalid Password", ApiExceptionConstants.INVALID_PASSWORD);
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
			throw new InvalidParameterException("Role IDs cannot be empty or null", ApiExceptionConstants.EMPTY_OR_NULL_ROLE_IDS);
		}
	}

	public void validateSearch(ApiUserSearchRequest contactSearchRequest) {
		if(contactSearchRequest == null
				|| (StringUtils.isEmpty(contactSearchRequest.getEmail())
				&& StringUtils.isEmpty(contactSearchRequest.getName()))
		) {
			throw new InvalidParameterException("Empty search criteria", ApiExceptionConstants.EMPTY_SEARCH_CRITERIA);
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
		isNullOrNotValidated(savedApiUser, ApiExceptionConstants.getUnregisteredEmail(newApiUser.getEmail()));
		if(StringUtils.isNotEmpty(password)) {
			validatePassword(password);
			validatePassword(password);
		}
	}
}
