package com.freela.service;

import com.freela.database.model.Device;
import com.freela.database.repository.DeviceRepository;
import com.freela.service.validator.DeviceValidator;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
@Transactional
public class DeviceService {
	private static final Logger log = LoggerFactory.getLogger(DeviceService.class);

	@Inject
	DeviceValidator deviceValidator;

	@Inject
	DeviceRepository deviceRepository;

	public Optional<Device> findByDeviceId(String deviceId) {
		log.info("findByDeviceId: { deviceId: {} }", deviceId);
		deviceValidator.validateDeviceId(deviceId);
		return deviceRepository.findByDeviceId(deviceId);
	}

	public Device create(Device device) {
		log.info("create: { device: {} }", device);
		deviceValidator.validateNew(device);
		return deviceRepository.save(device);
	}

	public Device retrieveOrCreate(@NonNull Device device) {
		log.info("retrieveOrCreate: { device: {} }", device);
		Optional<Device> dbDevice = findByDeviceId(device.getDeviceId());
		return dbDevice.orElseGet(() -> create(device));
	}
}
