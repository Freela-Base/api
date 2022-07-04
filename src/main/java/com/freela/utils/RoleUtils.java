package com.freela.utils;

import com.freela.database.enums.Role;
import jakarta.inject.Singleton;

import java.util.Collection;

@Singleton
public class RoleUtils {
	public boolean containsRoles(Collection<Role> userRoles, Collection<Role> expectedRoles) {
		if(expectedRoles != null && userRoles != null) {
			return expectedRoles.stream().anyMatch(userRoles::contains);
		}
		return false;
	}
}
