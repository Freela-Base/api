package com.freela.api.rest.authentication.enums;

public enum AuthAttributes {
	API_USER_ID("api_user_id"),
	ROLES("roles");
	private final String text;

	private AuthAttributes(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
