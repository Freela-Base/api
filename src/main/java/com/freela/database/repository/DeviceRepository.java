package com.freela.database.repository;

import com.freela.database.model.Device;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends PageableRepository<Device, Long> {
	@Executable
	Optional<Device> findByDeviceId(String deviceId);
}
