package com.freela.api.dto.parser;

import com.freela.api.dto.DeviceDto;
import com.freela.database.model.Device;
import jakarta.inject.Singleton;

@Singleton
public class DeviceParser  implements ParserInterface<Device, DeviceDto> {
	@Override
	public DeviceDto toDto(Device device) {
		if (device == null) {
			return null;
		}

		DeviceDto deviceDto = new DeviceDto();
		deviceDto.setId(deviceDto.getId());
		deviceDto.setDeviceId(deviceDto.getDeviceId());
		deviceDto.setDeviceName(deviceDto.getDeviceName());

		return deviceDto;
	}

	@Override
	public Device toModel(DeviceDto deviceDto) {
		if (deviceDto == null) {
			return null;
		}

		Device device = new Device();
		device.setId(deviceDto.getId());
		device.setDeviceId(deviceDto.getDeviceId());
		device.setDeviceName(deviceDto.getDeviceName());

		return device;
	}
}
