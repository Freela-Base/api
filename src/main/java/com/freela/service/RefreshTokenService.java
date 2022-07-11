package com.freela.service;

import com.freela.api.rest.authentication.enums.AuthAttributes;
import com.freela.database.enums.Role;
import com.freela.database.model.ApiUser;
import com.freela.database.model.Device;
import com.freela.database.model.RefreshToken;
import com.freela.database.repository.RefreshTokenRepository;
import com.freela.service.validator.RefreshTokenValidator;
import io.micronaut.context.annotation.Value;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Transactional
public class RefreshTokenService {
	private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

	@Value("${com.freela.service.refresh-token.expiration-time:86400}")
	private Long REFRESH_TOKEN_EXPIRATION_TIME;

	@Inject
	RefreshTokenValidator refreshTokenValidator;

	@Inject
	RefreshTokenRepository refreshTokenRepository;

	public Optional<RefreshToken> findByValue(String value) {
		log.info("findByValue: { value: {} }", value);
		return refreshTokenRepository.findByValue(value, OffsetDateTime.now());
	}

	public RefreshToken create(String refreshTokenValue, Device device, ApiUser apiUser) {
		log.info("create: { refreshTokenValue: {}, device: {}, apiUser: {} }", refreshTokenValue, device, apiUser);
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setApiUser(apiUser);
		refreshToken.setDevice(device);
		refreshToken.setValue(refreshTokenValue);
		refreshToken.setExpiration(OffsetDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME));
		refreshTokenValidator.validateNew(refreshToken);
		return refreshTokenRepository.save(refreshToken);
	}

	public AuthenticationResponse renewAuthentication(String value) {
		log.info("renewAuthentication: { value: {} }", value);

		Optional<RefreshToken> refreshToken = findByValue(value);
		if (refreshToken.isEmpty()) {
			throw AuthenticationResponse.exception(AuthenticationFailureReason.USER_NOT_FOUND);
		}

		ApiUser apiUser = refreshToken.get().getApiUser();
		Device device = refreshToken.get().getDevice();
		Collection<String> roles = Objects.requireNonNull(apiUser)
				.getRoles().stream()
				.map(Role::name)
				.collect(Collectors.toList());

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(AuthAttributes.API_USER_ID.toString(), apiUser.getId());
		attributes.put(AuthAttributes.DEVICE_ID.toString(), device.getDeviceId());
		return AuthenticationResponse.success(apiUser.getEmail(), roles, attributes);
	}
}
