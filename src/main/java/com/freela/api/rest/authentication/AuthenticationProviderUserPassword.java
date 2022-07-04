package com.freela.api.rest.authentication;

import com.freela.service.ApiUserService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);
	@Inject
	ApiUserService apiUserService;

	@Override
	public Publisher<AuthenticationResponse> authenticate(
			@Nullable HttpRequest<?> httpRequest,
			AuthenticationRequest<?, ?> authenticationRequest
	) {
		log.info("authenticate: { identity: {} }", authenticationRequest.getIdentity());
		return Flux.create(emitter -> {
			AuthenticationResponse userDetails = apiUserService.authenticate(
					(String)authenticationRequest.getIdentity(),
					(String)authenticationRequest.getSecret());
			if (userDetails != null) {
				emitter.next(userDetails);
				emitter.complete();
			} else {
				emitter.error(new AuthenticationException( new AuthenticationFailed()));
			}
		}, FluxSink.OverflowStrategy.ERROR);
	}
}
