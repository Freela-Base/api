package com.freela.service.validator;

import com.freela.database.enums.Role;
import com.freela.database.model.ApiUser;
import com.freela.exception.ApiException;
import com.freela.exception.ForbiddenException;
import com.freela.exception.InvalidParameterException;
import com.freela.exception.NotFoundException;
import com.freela.service.parameter.ApiUserSearchRequest;
import com.freela.utils.PasswordUtils;
import com.freela.utils.RoleUtils;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class ApiUserValidator {
	public static final List<Role> ADMIN_ROLE = new ArrayList<>() { { add(Role.ADMIN); } };
	public static final List<Role> CUSTOMER_ROLE = new ArrayList<>() { { add(Role.CUSTOMER); } };

	@Inject
	PasswordUtils passwordUtils;

	@Inject
	RoleUtils roleUtils;

	public void validateUserIdAndRoles(
			@NonNull Long userId,
			@NonNull Long authenticationId,
			@NonNull Collection<Role> authenticationRoles,
			@NonNull Collection<Role> validationRoles
	) {
		if(!userId.equals(authenticationId)) {
			validateRoles(authenticationRoles, validationRoles);
		}
	}

	public void validateRoles(@NonNull Collection<Role> userRoles, @NonNull Collection<Role> expectedRoles) {
		if(!roleUtils.containsRoles(userRoles, expectedRoles)) {
			throw new ForbiddenException("Resource not available");
		}
	}

	public void validatePassword(String password, ApiException.Source source) {
		//TODO add more complex validations for password
		// i.e. Upper Case and Lower Case letter, Number, Special Character (create regex?)
		if (password == null || password.length() < 8) {
			if(source != null) {
				source.setValue("***");
				source.setExpected("Min length: 8");
			}

			throw new InvalidParameterException("Invalid Password", source);
		}
	}

	public void validateUser(ApiUser apiUser, ApiException.Source source) {
		if (apiUser == null) {
			throw new NotFoundException("User not found", source);
		}
	}

	public void checkPassword(ApiUser apiUser, String password) {
		try {
			if (apiUser == null
					|| password == null
					|| apiUser.getPasswordHash() == null
					|| !apiUser.getPasswordHash().equals(passwordUtils.hash(
							password, apiUser.getPasswordSalt(), apiUser.getPasswordPepper()))
			) {
				throw new ForbiddenException("Invalid email or password");
			}
		} catch (Exception e) {
			//TODO log exception
			throw new ForbiddenException("Invalid email or password");
		}
	}

	public void isNullOrNotValidated(ApiUser apiUser, ApiException.Source source) {
		if(apiUser == null || !apiUser.isValidated()) {
			return;
		}

		throw new InvalidParameterException("User already registered", source);
	}

	public void validateSearch(ApiUserSearchRequest contactSearchRequest, Collection<Role> authenticationRoles) {
		if(contactSearchRequest == null
				|| (StringUtils.isEmpty(contactSearchRequest.getEmail())
				&& StringUtils.isEmpty(contactSearchRequest.getName()))
		) {
			throw new InvalidParameterException("Empty search criteria", new ApiException.Source(
					ApiException.Location.QUERY,
					"Search",
					"[email, name]",
					"Empty or null",
					"Valid search criteria"
			));
		}

		if(roleUtils.containsRoles(authenticationRoles, CUSTOMER_ROLE)) {
			List<Role> searchRoles = new ArrayList<>();
			searchRoles.add(Role.CUSTOMER);
			contactSearchRequest.setRoles(searchRoles);
		} else if (!roleUtils.containsRoles(authenticationRoles, ADMIN_ROLE)) {
			contactSearchRequest.getRoles().remove(Role.ADMIN);
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
				"User Creation",
				"email",
				newApiUser.getEmail(),
				"Unregistered email"
		));
		if(StringUtils.isNotEmpty(password)) {
			validatePassword(password, new ApiException.Source(
					ApiException.Location.BODY,
					"User Creation",
					"password"
			));
		}
	}
}
