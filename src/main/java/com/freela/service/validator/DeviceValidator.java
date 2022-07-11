package com.freela.service.validator;

import com.freela.database.model.Device;
import com.freela.exception.ApiException;
import com.freela.exception.InvalidParameterException;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

@Singleton
public class DeviceValidator {

	public void validateNew(Device device) {
		if (device == null) {
			throw new InvalidParameterException("Invalid Device information", new ApiException.Source(
					ApiException.Location.BODY,
					"device",
					"null",
					"Valid Device object"
			));
		}

		validateDeviceId(device.getDeviceId());
	}

	public void validateDeviceId(String deviceId) {
		if(StringUtils.isEmpty(deviceId)) {
			throw new InvalidParameterException("Invalid Device information", new ApiException.Source(
					ApiException.Location.BODY,
					"deviceId",
					String.format("%s", deviceId),
					"Valid deviceId"
			));
		}
	}
}
