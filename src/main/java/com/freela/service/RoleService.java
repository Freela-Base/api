package com.freela.service;

import com.freela.database.model.Role;
import com.freela.database.repository.RoleRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Singleton
@Transactional
public class RoleService {
	@Inject
	RoleRepository roleRepository;

	public Set<Role> findByNameIn(@NonNull List<String> values) {
		return roleRepository.findByNameIn(values);
	}
}
