package com.freela.database.repository;

import com.freela.database.model.Role;
import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.Set;


@Repository
public interface RoleRepository extends PageableRepository<Role, Long> {

	@Executable
	Boolean existsByName(@NonNull String value);

	@Executable
	Set<Role> findByNameIn(@NonNull List<String> values);
}