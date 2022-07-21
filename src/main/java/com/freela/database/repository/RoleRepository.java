package com.freela.database.repository;

import com.freela.database.model.Role;
import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.util.Collection;
import java.util.Set;


@Repository
public interface RoleRepository extends PageableRepository<Role, Long> {

	@Executable
	Boolean existsByName(@NonNull String value);

	@Executable
	Set<Role> findByNameIn(@NonNull Collection<String> names);

	@Executable
	Set<Role> findByIdIn(@NonNull Collection<Long> roleIds);

	@Executable
	void update(
			@Id Long id,
			@NonNull Boolean deleted);
}