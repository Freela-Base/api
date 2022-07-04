package com.freela.api.rest.authentication;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Singleton
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {

	@Override
	public void persistToken(RefreshTokenGeneratedEvent event) {
		//TODO save new refresh token
		if (event != null &&
				event.getRefreshToken() != null &&
				event.getAuthentication() != null &&
				event.getAuthentication().getName() != null
		) {
			String payload = event.getRefreshToken();
		}
	}

	@Override
	public Publisher<Authentication> getAuthentication(String refreshToken) {
		//TODO validate if refresh token is valid

		return Flux.create(emitter -> {
			emitter.next(Authentication.build("Test"));
			emitter.complete();
		}, FluxSink.OverflowStrategy.ERROR);
	}
}
