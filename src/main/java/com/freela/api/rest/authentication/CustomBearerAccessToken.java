package com.freela.api.rest.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

import java.util.Collection;

@Bean
public class CustomBearerAccessToken extends BearerAccessRefreshToken {

	@JsonProperty("api_user_id")
	private Long apiUserId;

	@JsonProperty("expires_at")
	private String expiresAt;

	public CustomBearerAccessToken(
			String username,
			Collection<String> roles,
			Integer expiresIn,
			String accessToken,
			String refreshToken,
			String tokenType,
			Long apiUserId,
			String expiresAt
	) {
		super(username, roles, expiresIn, accessToken, refreshToken, tokenType);
		this.apiUserId = apiUserId;
		this.expiresAt = expiresAt;
	}

	public Long getApiUserId() {
		return apiUserId;
	}

	public void setApiUserId(Long apiUserId) {
		this.apiUserId = apiUserId;
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}
}
