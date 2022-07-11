package com.freela.api.rest.controller;

import com.freela.api.dto.CredentialsDto;
import io.micronaut.context.event.ApplicationEvent;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.Authenticator;
import io.micronaut.security.event.LoginFailedEvent;
import io.micronaut.security.event.LoginSuccessfulEvent;
import io.micronaut.security.handlers.LoginHandler;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@ExecuteOn(TaskExecutors.IO)
@Controller("/login")
@Secured(SecurityRule.IS_ANONYMOUS)
public class LoginController {
	@Inject
	Authenticator authenticator;

	@Inject
	LoginHandler loginHandler;

	@Inject
	ApplicationEventPublisher<ApplicationEvent> eventPublisher;

	@Post
	public Publisher<MutableHttpResponse<?>> login(
			@Body @Valid CredentialsDto usernamePasswordCredentials,
			HttpRequest<?> request
	) {
		return Flux.from(this.authenticator.authenticate(request, usernamePasswordCredentials)).map((authenticationResponse) -> {
			if (authenticationResponse.isAuthenticated() && authenticationResponse.getAuthentication().isPresent()) {
				Authentication authentication = authenticationResponse.getAuthentication().get();
				this.eventPublisher.publishEvent(new LoginSuccessfulEvent(authentication));
				return this.loginHandler.loginSuccess(authentication, request);
			} else {
				this.eventPublisher.publishEvent(new LoginFailedEvent(authenticationResponse));
				return this.loginHandler.loginFailed(authenticationResponse, request);
			}
		}).defaultIfEmpty(HttpResponse.status(HttpStatus.UNAUTHORIZED));
	}
}
