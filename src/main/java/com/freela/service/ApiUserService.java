package com.freela.service;

import com.freela.api.rest.authentication.enums.AuthAttributes;
import com.freela.database.enums.Role;
import com.freela.database.model.ApiUser;
import com.freela.database.model.Device;
import com.freela.database.repository.ApiUserRepository;
import com.freela.exception.ApiException;
import com.freela.service.parameter.ApiUserSearchRequest;
import com.freela.service.parameter.PageRequest;
import com.freela.service.validator.ApiUserValidator;
import com.freela.service.validator.FieldValidator;
import com.freela.service.validator.PageValidator;
import com.freela.utils.PasswordUtils;
import com.freela.utils.RoleUtils;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.security.spec.InvalidKeySpecException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Transactional
public class ApiUserService {
	private static final Logger log = LoggerFactory.getLogger(ApiUserService.class);

	//TODO Set this as configuration in application.yaml
	private static final Integer RECOVERY_CODE_SIZE = 128;
	private static final Integer RECOVERY_CODE_VALID_HOURS = 3;
	private static final Integer PASSWORD_SALT_SIZE = 32;
	private static final Integer PASSWORD_PEPPER_SIZE = 32;

	@Inject
	PasswordUtils passwordUtils;

	@Inject
	PageValidator pageValidator;

	@Inject
	FieldValidator fieldValidator;

	@Inject
	ApiUserValidator apiUserValidator;

	@Inject
	ApiUserRepository apiUserRepository;

	@Inject
	DeviceService deviceService;

	@Inject
	RoleUtils roleUtils;

	public ApiUser create(
			@NonNull ApiUser newApiUser,
			@NonNull String password
	) throws InvalidKeySpecException {
		log.info("save: { newApiUser: {}, isPasswordPresent: {} }",
				newApiUser, StringUtils.isNotEmpty(password) ? "true" : "false");

		// Check if there is an entry for that user
		ApiUser savedApiUser = apiUserRepository.findByEmail(newApiUser.getEmail()).orElse(null);
		apiUserValidator.validateUserToSave(newApiUser, savedApiUser, password);

		//Set recovery information for user validation
		newApiUser.setRecoveryCode(passwordUtils.getRandomString(RECOVERY_CODE_SIZE));
		newApiUser.setRecoveryCodeValidUntil(OffsetDateTime.now().plusHours(RECOVERY_CODE_VALID_HOURS));
		newApiUser.setRoles(Set.of(Role.CUSTOMER));

		setApiUserPassword(newApiUser, password);

		if(savedApiUser == null) {
			newApiUser = apiUserRepository.save(newApiUser);
		} else if (!savedApiUser.isValidated()) {
			apiUserRepository.update(
					savedApiUser.getId(),
					newApiUser.getName(),
					savedApiUser.getEmail(),
					savedApiUser.getPhoneNumber(),
					newApiUser.getBirthDate(),
					newApiUser.getPasswordHash(),
					newApiUser.getPasswordSalt(),
					newApiUser.getPasswordPepper(),
					newApiUser.getRecoveryCode(),
					newApiUser.getRecoveryCodeValidUntil()
			);
		}
		// TODO send phone number / email confirmation
		return newApiUser;
	}

	private void setApiUserPassword(
			@NonNull ApiUser apiUser,
			@NonNull String password
	) throws InvalidKeySpecException {
		if (StringUtils.isNotEmpty(password)) {
			apiUser.setPasswordSalt(passwordUtils.getRandomString(PASSWORD_SALT_SIZE));
			apiUser.setPasswordPepper(passwordUtils.getRandomString(PASSWORD_PEPPER_SIZE));
			apiUser.setPasswordHash(passwordUtils.hash(
					password,
					apiUser.getPasswordSalt(),
					apiUser.getPasswordPepper()));
		}
	}

	public Optional<ApiUser> getById(Long id, Long authenticationId, Set<Role> authenticationRoles) {
		log.info("findById: { apiUserRequestId: {}, authenticationId: {}, authenticationRoles: {} }",
				id, authenticationId, authenticationRoles);
		apiUserValidator.validateUserIdAndRoles(id, authenticationId, authenticationRoles, ApiUserValidator.ADMIN_ROLE);
		return apiUserRepository.findById(id);
	}

	public AuthenticationResponse authenticate(String email, String password, Device device) {
		log.info("authenticate: { email: {}, isPasswordPresent: {}, device: {} }",
				email, StringUtils.isNotEmpty(password) ? "true" : "false", device);

		ApiUser apiUser = apiUserRepository.findByEmailAndValidatedTrueAndDeletedFalse(email).orElse(null);
		apiUserValidator.checkPassword(apiUser, password);
		device = deviceService.retrieveOrCreate(device);

		Collection<String> roles = Objects.requireNonNull(apiUser)
				.getRoles().stream()
				.map(Role::name)
				.collect(Collectors.toList());

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(AuthAttributes.API_USER_ID.toString(), apiUser.getId());
		attributes.put(AuthAttributes.DEVICE_ID.toString(), device.getDeviceId());
		return AuthenticationResponse.success(apiUser.getEmail(), roles, attributes);
	}

	public void update(
			Long apiUserid,
			ApiUser newApiUser,
			Long authenticationId,
			Collection<Role> authenticationRoles
	) {
		log.info("update: { apiUserid: {}, newApiUser: {}, authenticationId: {}, authenticationRoles: {} }",
				apiUserid, newApiUser, authenticationId, authenticationRoles);
		apiUserValidator.validateUserIdAndRoles(
				apiUserid, authenticationId, authenticationRoles, ApiUserValidator.ADMIN_ROLE);
		apiUserRepository.update(
				apiUserid,
				newApiUser.getName(),
				newApiUser.getEmail(),
				newApiUser.getPhoneNumber(),
				newApiUser.getBirthDate()
		);
	}

	public Page<ApiUser> findAll(PageRequest pageRequest) {
		log.info("findAll: { pageRequest: {} }", pageRequest);

		pageValidator.validate(pageRequest);
		return apiUserRepository.findAll(Pageable.from(
				pageRequest.getPageNumber(),
				pageRequest.getPageSize(),
				Sort.of(Sort.Order.asc("name"), Sort.Order.asc("email"))));
	}

	public void validateApiUser(
			@NonNull String recoveryCode,
			@NonNull String password
	) throws InvalidKeySpecException {
		log.info("validateApiUser: { recoveryCode: {}, isPasswordPresent: {} }",
				recoveryCode, StringUtils.isNotEmpty(password) ? "true" : "false");

		ApiUser apiUser = apiUserRepository.findByRecoveryCodeAndDeletedFalse(recoveryCode).orElse(null);

		apiUserValidator.validateUser(apiUser, new ApiException.Source(
				ApiException.Location.BODY,
				"recoveryCode",
				recoveryCode,
				"Valid Token"
		));

		if(Objects.requireNonNull(apiUser).isValidated()
				|| StringUtils.isNotEmpty(password) && apiUser.getPasswordHash() == null
		) {
			apiUserValidator.validatePassword(password);
			setApiUserPassword(apiUser, password);
		}

		apiUserRepository.update(
				apiUser.getId(),
				true,
				apiUser.getPasswordHash(),
				apiUser.getPasswordSalt(),
				apiUser.getPasswordPepper(),
				null,
				null
		);
	}

	public void forgotPassword(@NonNull String email) {
		log.info("forgotPassword: { email: {} }", email);

		fieldValidator.validateString(email, new ApiException.Source(
				ApiException.Location.QUERY,
				"email",
				"Empty or Null",
				"User email"
		));

		ApiUser apiUser = apiUserRepository.findByEmailAndValidatedTrueAndDeletedFalse(email).orElse(null);

		apiUserValidator.validateUser(apiUser, new ApiException.Source(
				ApiException.Location.QUERY,
				"email",
				email,
				"User email"
		));

		apiUserRepository.update(
				Objects.requireNonNull(apiUser).getId(),
				passwordUtils.getRandomString(RECOVERY_CODE_SIZE),
				OffsetDateTime.now().plusHours(RECOVERY_CODE_VALID_HOURS)
		);

		// TODO send email with recovery code
	}

	public Page<ApiUser> search(
			@NonNull PageRequest pageRequest,
			@NonNull ApiUserSearchRequest apiUserSearchRequest,
			@NonNull Collection<Role> authenticationRoles
	) {
		log.info("search: { apiUserSearchRequest: {}, authenticationRoles: {}, pageRequest: {} }",
				apiUserSearchRequest, authenticationRoles, pageRequest);

		pageValidator.validate(pageRequest);
		apiUserValidator.validateSearch(apiUserSearchRequest, authenticationRoles);

		if(roleUtils.containsRoles(authenticationRoles, Collections.singletonList(Role.ADMIN))) {
			return apiUserRepository.findByEmailOrName(
					apiUserSearchRequest.getName(),
					apiUserSearchRequest.getEmail(),
					apiUserSearchRequest.getRoles(),
					Pageable.from(
							pageRequest.getPageNumber(),
							pageRequest.getPageNumber(),
							Sort.of(Sort.Order.asc("name")))
			);
		} else {
			return apiUserRepository.findByEmailOrNameAndValidated(
					apiUserSearchRequest.getName(),
					apiUserSearchRequest.getEmail(),
					apiUserSearchRequest.getRoles(),
					Pageable.from(
							pageRequest.getPageNumber(),
							pageRequest.getPageNumber(),
							Sort.of(Sort.Order.asc("name")))
			);
		}
	}
}
