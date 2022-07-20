package com.freela.service;

import com.freela.database.enums.ApiAction;
import com.freela.database.model.Role;
import com.freela.database.repository.RoleRepository;
import com.freela.service.parameter.PageRequest;
import com.freela.service.validator.PageValidator;
import com.freela.service.validator.RoleValidator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Singleton
@Transactional
public class RoleService {

	private static final Logger log = LoggerFactory.getLogger(RoleService.class);

	@Inject
	ApiUserService apiUserService;

	@Inject
	RoleRepository roleRepository;

	@Inject
	RoleValidator roleValidator;

	@Inject
	PageValidator pageValidator;

	public Set<Role> findByNameIn(@NonNull Collection<String> values) {
		return roleRepository.findByNameIn(values);
	}

	public Role create(Role role, Set<ApiAction> permittedApiActions) {
		log.info("create: { role: {}, permittedApiActions: {} }", role, permittedApiActions);

		// Make sure ID is null
		role.setId(null);

		roleValidator.validateCreate(role, permittedApiActions);
		return roleRepository.save(role);
	}

	public Role update(
			@NonNull Long roleId,
			Role newRole,
			Set<ApiAction> permittedApiActions
	) {
		log.info("update: { roleId: {}, newRole: {}, permittedApiActions: {} }", roleId, newRole, permittedApiActions);
		Role role = roleRepository.findById(roleId).orElse(null);
		roleValidator.validateExists(roleId, role);
		roleValidator.validateUpdate(Objects.requireNonNull(role), newRole, permittedApiActions);
		role.setName(newRole.getName());
		role.setApiActions(newRole.getApiActions());
		return roleRepository.update(role);
	}

	public void delete(
			@NonNull Long roleId,
			Set<ApiAction> permittedApiActions
	) {
		log.info("delete: { roleId: {}, permittedApiActions: {} }", roleId, permittedApiActions);
		Role role = roleRepository.findById(roleId).orElse(null);
		roleValidator.validateExists(roleId, role);
		roleValidator.validateDelete(
				Objects.requireNonNull(role),
				apiUserService.areRolesBeingUsed(List.of(role.getId())),
				permittedApiActions);
		roleRepository.update(role.getId(), true);
	}

	public Page<Role> findAll(
			@NonNull PageRequest pageRequest
	) {
		log.info("findAll: { pageRequest: {} }", pageRequest);
		pageValidator.validate(pageRequest);
		return roleRepository.findAll(Pageable.from(
				pageRequest.getPageNumber(),
				pageRequest.getPageSize(),
				Sort.of(Sort.Order.asc("name"))));
	}
}
