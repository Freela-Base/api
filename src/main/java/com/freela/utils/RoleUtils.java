package com.freela.utils;

import com.freela.database.enums.ApiAction;
import com.freela.database.model.Role;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class RoleUtils {
	public boolean containsApiAction(Collection<Role> userRoles, Collection<ApiAction> expectedApiActions) {
		if(expectedApiActions != null && userRoles != null) {
			return userRoles.stream()
					.map(Role::getApiActions)
					.flatMap(Set::stream)
					.anyMatch(expectedApiActions::contains);
		}
		return false;
	}

	public Set<String> getApiActions(Collection<Role> roles) {
		Set<String> apiActions = new HashSet<>();
		if (CollectionUtils.isNotEmpty(roles)) {
			roles.stream()
					.map(Role::getApiActions)
					.flatMap(Set::stream)
					.map(ApiAction::name)
					.forEach(apiActions::add);
		}
		return apiActions;
	}

}
