package com.freela.database.repository;

import com.freela.database.model.RefreshToken;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.time.OffsetDateTime;
import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends PageableRepository<RefreshToken, Long> {

	@Executable
	@Query(value = "SELECT refreshToken_ FROM RefreshToken refreshToken_ WHERE refreshToken_.value = :value and refreshToken_.expiration >= :currentTime ")
	Optional<RefreshToken> findByValue(String value, OffsetDateTime currentTime);
}