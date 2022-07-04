package com.freela.api.rest.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

import java.util.Collection;

@Bean
public class CustomBearerAccessToken extends BearerAccessRefreshToken {

	@JsonProperty("api_user_id")
	private Long apiUserId;

//TODO change refresh token type to CustomBearerAccessToken
	public CustomBearerAccessToken(
			String username,
			Collection<String> roles,
			Integer expiresIn,
			String accessToken,
			String refreshToken,
			String tokenType,
			Long apiUserId
	) {
		super(username, roles, expiresIn, accessToken, refreshToken, tokenType);
		this.apiUserId = apiUserId;
	}

	public Long getApiUserId() {
		return apiUserId;
	}

	public void setApiUserId(Long apiUserId) {
		this.apiUserId = apiUserId;
	}
}
