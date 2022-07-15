package com.freela.api.rest.authentication;

import com.freela.database.enums.ApiAction;
import com.freela.database.model.Role;
import com.freela.service.RoleService;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.token.TokenAuthenticationFetcher;
import io.micronaut.security.token.reader.TokenResolver;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Replaces(TokenAuthenticationFetcher.class)
public class CustomTokenAuthenticationFetcher extends TokenAuthenticationFetcher {
	private static final Logger log = LoggerFactory.getLogger(CustomTokenAuthenticationFetcher.class);

	private final RoleService roleService;
	/**
	 * @param tokenValidators The list of {@link TokenValidator} which attempt to validate the request
	 * @param tokenResolver   The {@link TokenResolver} which returns the first found token in the request.
	 * @param eventPublisher  The Application event publisher
	 */
	public CustomTokenAuthenticationFetcher(
			Collection<TokenValidator> tokenValidators,
			TokenResolver tokenResolver,
			ApplicationEventPublisher eventPublisher,
			RoleService roleService
	) {
		super(tokenValidators, tokenResolver, eventPublisher);
		this.roleService = roleService;
	}

	@Override
	public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
		log.info("Replacing Roles for API Actions");
		return Flux.from(super.fetchAuthentication(request)).map(authentication -> {
			List<String> roleNames = new ArrayList<>(authentication.getRoles());
			Set<Role> roles = roleService.findByNameIn(roleNames);
			Set<String> apiActionNames = roles.stream()
					.map(Role::getApiActions)
					.flatMap(Set::stream)
					.map(ApiAction::name)
					.collect(Collectors.toSet());
			apiActionNames.addAll(authentication.getRoles());

			Optional<Authentication> newAuthentication = AuthenticationResponse
					.success(authentication.getName(), apiActionNames, authentication.getAttributes())
					.getAuthentication();

			return newAuthentication.orElse(authentication);
		});
	}
}
