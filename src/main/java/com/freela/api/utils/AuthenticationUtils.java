package com.freela.api.utils;

import com.freela.api.rest.authentication.enums.AuthAttributes;
import com.freela.database.enums.ApiAction;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class AuthenticationUtils {
	public Long getApiUserId(Authentication authentication) {
		return (Long) authentication.getAttributes().get(AuthAttributes.API_USER_ID.toString());
	}

	public String getDeviceId(Authentication authentication) {
		return (String) authentication.getAttributes().get(AuthAttributes.DEVICE_ID.toString());
	}

	public Set<ApiAction> getApiActions(Authentication authentication) {
		Set<ApiAction> apiActions = new HashSet<>();
		if (authentication == null
				|| CollectionUtils.isEmpty(authentication.getRoles())
		) {
			return apiActions;
		}

		authentication.getRoles().forEach(r -> apiActions.add(ApiAction.valueOf(r)));
		return apiActions;
	}
}
