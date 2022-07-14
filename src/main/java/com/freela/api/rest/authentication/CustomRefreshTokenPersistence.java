package com.freela.api.rest.authentication;

import com.freela.api.utils.AuthenticationUtils;
import com.freela.database.model.ApiUser;
import com.freela.database.model.Device;
import com.freela.database.model.RefreshToken;
import com.freela.service.ApiUserService;
import com.freela.service.DeviceService;
import com.freela.service.RefreshTokenService;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Optional;

@Singleton
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {
	private static final Logger log = LoggerFactory.getLogger(CustomRefreshTokenPersistence.class);

	@Inject
	DeviceService deviceService;

	@Inject
	ApiUserService apiUserService;

	@Inject
	RefreshTokenService refreshTokenService;

	@Inject
	AuthenticationUtils authenticationUtils;

	private boolean isValidRefreshTokenEvent(RefreshTokenGeneratedEvent event) {
		return event != null
				&& event.getRefreshToken() != null
				&& event.getAuthentication() != null;
	}

	@Override
	public void persistToken(RefreshTokenGeneratedEvent event) {
		log.info("persistToken");
		if (isValidRefreshTokenEvent(event)) {
			Long apiUserId = authenticationUtils.getApiUserId(event.getAuthentication());
			String deviceId = authenticationUtils.getDeviceId(event.getAuthentication());

			Optional<Device> device = deviceService.findByDeviceId(deviceId);
			Optional<ApiUser> apiUser = apiUserService.getById(apiUserId);

			RefreshToken refreshToken = refreshTokenService.create(
					event.getRefreshToken(),
					device.orElse(null),
					apiUser.orElse(null));
			log.info("persistToken: { refreshToken: {} }", refreshToken.getId());
		}
	}

	@Override
	public Publisher<Authentication> getAuthentication(String refreshTokenValue) {
		log.info("getAuthentication: { refreshTokenValue: {} }", refreshTokenValue);
		return Flux.create(emitter -> {
			AuthenticationResponse authenticationResponse = refreshTokenService.renewAuthentication(refreshTokenValue);
			if (authenticationResponse.getAuthentication().isPresent()) {
				emitter.next(authenticationResponse.getAuthentication().get());
				emitter.complete();
			} else {
				emitter.error(new AuthenticationException(new AuthenticationFailed()));
			}
		}, FluxSink.OverflowStrategy.ERROR);
	}
}
