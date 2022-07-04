package com.freela.api.rest.authentication;

import com.freela.api.rest.authentication.enums.AuthAttributes;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
@Replaces(bean = BearerTokenRenderer.class)
public class CustomBearerTokenRenderer extends BearerTokenRenderer {
	@Override
	public AccessRefreshToken render(
			Authentication userDetails,
			Integer expiresIn,
			String accessToken,
			String refreshToken
	) {
		Map<String, Object> attributes = userDetails.getAttributes();
		Long contactId = (Long) attributes.get(AuthAttributes.API_USER_ID.toString());
		return new CustomBearerAccessToken(
				userDetails.getName(),
				userDetails.getRoles(),
				expiresIn,
				accessToken,
				refreshToken,
				"Bearer",
				contactId
		);
	}
}
