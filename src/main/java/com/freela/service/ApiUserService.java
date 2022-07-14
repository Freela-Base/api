package com.freela.service;

import com.freela.api.rest.authentication.enums.AuthAttributes;
import com.freela.database.model.ApiUser;
import com.freela.database.model.Device;
import com.freela.database.model.Role;
import com.freela.database.repository.ApiUserRepository;
import com.freela.database.repository.RoleRepository;
import com.freela.exception.ApiException;
import com.freela.service.parameter.ApiUserSearchRequest;
import com.freela.service.parameter.PageRequest;
import com.freela.service.validator.ApiUserValidator;
import com.freela.service.validator.FieldValidator;
import com.freela.service.validator.PageValidator;
import com.freela.utils.PasswordUtils;
import com.freela.utils.RoleUtils;
import io.micronaut.context.annotation.Value;
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

@Singleton
@Transactional
public class ApiUserService {
	private static final Logger log = LoggerFactory.getLogger(ApiUserService.class);

	@Value("${com.freela.service.api-user.recovery-code.size:128}")
	private Integer RECOVERY_CODE_SIZE;

	@Value("${com.freela.service.api-user.recovery-code.valid-time:10800}")
	private Integer RECOVERY_CODE_VALID_TIME;

	@Value("${com.freela.service.api-user.create.default-role:DEFAULT_USER}")
	private String CREATE_API_USER_DEFAULT_ROLE;

	@Inject
	PasswordUtils passwordUtils;

	@Inject
	RoleUtils roleUtils;

	@Inject
	PageValidator pageValidator;

	@Inject
	FieldValidator fieldValidator;

	@Inject
	ApiUserValidator apiUserValidator;

	@Inject
	ApiUserRepository apiUserRepository;

	@Inject
	RoleRepository roleRepository;

	@Inject
	DeviceService deviceService;

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
		newApiUser.setRecoveryCodeValidUntil(OffsetDateTime.now().plusSeconds(RECOVERY_CODE_VALID_TIME));

		Set<Role> userRoles = roleRepository.findByNameIn(List.of(CREATE_API_USER_DEFAULT_ROLE.split(",")));
		newApiUser.setRoles(userRoles);
		if (userRoles.isEmpty()) {
			log.warn("Creating user without roles: { email: {} }", newApiUser.getEmail());
		}

		passwordUtils.setApiUserPassword(newApiUser, password);

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



	public Optional<ApiUser> getById(Long id) {
		log.info("findById: { apiUserRequestId: {} }",
				id);
		return apiUserRepository.findById(id);
	}

	public AuthenticationResponse authenticate(String email, String password, Device device) {
		log.info("authenticate: { email: {}, isPasswordPresent: {}, device: {} }",
				email, StringUtils.isNotEmpty(password) ? "true" : "false", device);

		ApiUser apiUser = apiUserRepository.findByEmailAndValidatedTrueAndDeletedFalse(email).orElse(null);
		apiUserValidator.checkPassword(apiUser, password);
		device = deviceService.retrieveOrCreate(device);
		//TODO check if it is possible to send roles instead api actions here
		Set<String> roles = roleUtils.getApiActions(Objects.requireNonNull(apiUser).getRoles());

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(AuthAttributes.API_USER_ID.toString(), apiUser.getId());
		attributes.put(AuthAttributes.DEVICE_ID.toString(), device.getDeviceId());
		return AuthenticationResponse.success(apiUser.getEmail(), roles, attributes);
	}

	public void update(
			Long apiUserid,
			ApiUser newApiUser
	) {
		log.info("update: { apiUserid: {}, newApiUser: {} }",
				apiUserid, newApiUser);
		apiUserRepository.update(
				apiUserid,
				newApiUser.getName(),
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
			passwordUtils.setApiUserPassword(apiUser, password);
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
				OffsetDateTime.now().plusSeconds(RECOVERY_CODE_VALID_TIME)
		);

		// TODO send email with recovery code
	}

	public Page<ApiUser> search(
			@NonNull PageRequest pageRequest,
			@NonNull ApiUserSearchRequest apiUserSearchRequest
	) {
		log.info("search: { apiUserSearchRequest: {}, pageRequest: {} }",
				apiUserSearchRequest, pageRequest);

		pageValidator.validate(pageRequest);
		apiUserValidator.validateSearch(apiUserSearchRequest);

		return apiUserRepository.findByEmailOrNameAndValidated(
				apiUserSearchRequest.getName(),
				apiUserSearchRequest.getEmail(),
				Pageable.from(
						pageRequest.getPageNumber(),
						pageRequest.getPageNumber(),
						Sort.of(Sort.Order.asc("name"))));
	}
}
