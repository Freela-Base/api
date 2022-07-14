package com.freela.database.repository;

import com.freela.database.model.ApiUser;
import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface ApiUserRepository extends PageableRepository<ApiUser, Long> {
	@Executable
	Optional<ApiUser> findByEmailAndValidatedTrueAndDeletedFalse(String email);
	@Executable
	Optional<ApiUser> findByEmail(String email);
	@Executable
	Optional<ApiUser> findByRecoveryCodeAndDeletedFalse(String recoveryCode);

	@Executable
	@Query(value = "SELECT user_ FROM ApiUser user_ WHERE (lower(user_.name) like lower(concat('%', :name, '%')) or lower(user_.email) = lower(:email)) and user_.validated is true and user_.deleted is false",
			countQuery = "SELECT count(user_) FROM ApiUser user_ WHERE (lower(user_.name) like lower(concat('%', :name, '%')) or lower(user_.email) = lower(:email)) and user_.validated is true and user_.deleted is false")
	Page<ApiUser> findByEmailOrNameAndValidated (
			@Nullable String name,
			@Nullable String email,
			Pageable pageable
	);

	@Executable
	@Query(value = "SELECT user_ FROM ApiUser user_ WHERE (lower(user_.name) like lower(concat('%', :name, '%')) or lower(user_.email) = lower(:email)) and user_.deleted is false",
			countQuery = "SELECT count(user_) FROM ApiUser user_ WHERE (lower(user_.name) like lower(concat('%', :name, '%')) or lower(user_.email) = lower(:email)) and user_.deleted is false")
	Page<ApiUser> findByEmailOrName (
			@Nullable String name,
			@Nullable String email,
			Pageable pageable
	);

	@Executable
	void update(
			@Id Long id,
			@NotBlank String name,
			@NotBlank String email,
			@NotBlank String phoneNumber,
			@Nullable LocalDate birthDate,
			@Nullable String passwordHash,
			@Nullable String passwordSalt,
			@Nullable String passwordPepper,
			@NonNull String recoveryCode,
			@NonNull OffsetDateTime recoveryCodeValidUntil
	);

	@Executable
	void update(
			@Id Long id,
			@NotBlank String name,
			@NotBlank String phoneNumber,
			@Nullable LocalDate birthDate
	);

	@Executable
	void update(
			@Id Long id,
			@NonNull Boolean validated,
			@NotBlank String passwordHash,
			@Nullable String passwordSalt,
			@Nullable String passwordPepper,
			@Nullable String recoveryCode,
			@Nullable OffsetDateTime recoveryCodeValidUntil
	);

	@Executable
	void update(
			@Id Long id,
			@Nullable String recoveryCode,
			@Nullable OffsetDateTime recoveryCodeValidUntil
	);
}
