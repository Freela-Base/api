package com.freela.api.utils;

import com.freela.api.rest.authentication.enums.AuthAttributes;
import com.freela.database.enums.ApiAction;
import com.freela.utils.RoleUtils;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class AuthenticationUtils {
	@Inject
	RoleUtils roleUtils;

	public Long getApiUserId(Authentication authentication) {
		return (Long) authentication.getAttributes().get(AuthAttributes.API_USER_ID.toString());
	}

	public String getDeviceId(Authentication authentication) {
		return (String) authentication.getAttributes().get(AuthAttributes.DEVICE_ID.toString());
	}

	public Set<ApiAction> getApiActions(Authentication authentication) {
		if(authentication ==  null) {
			return new HashSet<>();
		}

		return roleUtils.getApiActionsFromStr(authentication.getRoles());
	}
}
