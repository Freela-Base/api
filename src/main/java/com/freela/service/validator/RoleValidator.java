package com.freela.service.validator;

import com.freela.database.enums.ApiAction;
import com.freela.database.model.Role;
import com.freela.exception.ApiException;
import com.freela.exception.InvalidParameterException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class RoleValidator {

	@Inject
	FieldValidator fieldValidator;

	public void validateExists(Long roleId, Role role) {
		if (role == null) {
			throw new InvalidParameterException("Role not found", new ApiException.Source(
					ApiException.Location.BODY,
					"RoleId",
					"%s".formatted(roleId),
					"Valid Role ID"));
		}
	}

	public void validateUpdate(
			@NonNull Role role,
			@NonNull Role newRole,
			Set<ApiAction> permittedApiActions
	) {
		validateName(newRole.getName());
		validateApiActionsNotEmpty(newRole.getApiActions());
		validatePermittedApiActions(permittedApiActions);
		validatePreviousApiActions(role, permittedApiActions);
		validatePermittedApiActions(newRole.getApiActions(), permittedApiActions);
	}

	public void validateDelete(
			@NonNull Role role,
			@NonNull Boolean isBeingUsed,
			Set<ApiAction> permittedApiActions
	) {
		validatePermittedApiActions(permittedApiActions);
		validatePreviousApiActions(role, permittedApiActions);

		if (Boolean.TRUE.equals(isBeingUsed)) {
			throw new InvalidParameterException("Cannot delete Role that is being used by an user", new ApiException.Source(
					ApiException.Location.BODY,
					"roleId",
					"%s".formatted(role.getId()),
					"Remove role from all users before deleting it"));
		}
	}

	public void validateCreate(
			@NonNull Role role,
			Set<ApiAction> permittedApiActions
	) {
		validateName(role.getName());
		validateApiActionsNotEmpty(role.getApiActions());
		validatePermittedApiActions(permittedApiActions);
		validatePermittedApiActions(role.getApiActions(), permittedApiActions);
	}

	public void validateName(String name) {
		fieldValidator.validateString(name, new ApiException.Source(
				ApiException.Location.BODY,
				"name",
				"%s".formatted(name),
				"Role name cannot be null or empty"));
	}

	public void validatePermittedApiActions(
			@NonNull Set<ApiAction> apiActions,
			@NonNull Set<ApiAction> permittedApiActions
	) {
		if (!permittedApiActions.containsAll(apiActions)) {
			throw new InvalidParameterException("API Actions need to be subset of user's API actions", new ApiException.Source(
					ApiException.Location.BODY,
					"apiActions",
					"%s".formatted(apiActions),
					"Valid subset of user's API Actions"));
		}
	}

	public void validatePreviousApiActions(
			@NonNull Role role,
			@NonNull Set<ApiAction> permittedApiActions
	) {
		if (!permittedApiActions.containsAll(role.getApiActions())) {
			throw new InvalidParameterException("API Actions need to be subset of user's API actions", new ApiException.Source(
					ApiException.Location.BODY,
					"RoleId",
					"%s".formatted(role.getId()),
					"Role must contains subset of user's API Actions to be updated"));
		}
	}

	public void validatePermittedApiActions(Set<ApiAction> permittedApiActions) {
		if (CollectionUtils.isEmpty(permittedApiActions)) {
			throw new RuntimeException("Permitted API Actions cannot be null or empty");
		}
	}

	public void validateApiActionsNotEmpty(Set<ApiAction> apiActions) {
		if (CollectionUtils.isEmpty(apiActions)) {
			throw new InvalidParameterException("Api Actions cannot be null or empty", new ApiException.Source(
					ApiException.Location.BODY,
					"apiActions",
					"%s".formatted(apiActions),
					"Valid set of API Actions"));
		}
	}
}
