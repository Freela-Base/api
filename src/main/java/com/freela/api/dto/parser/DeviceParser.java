package com.freela.api.dto.parser;

import com.freela.api.dto.DeviceDto;
import com.freela.database.model.Device;
import jakarta.inject.Singleton;

@Singleton
public class DeviceParser  implements ParserInterface<Device, DeviceDto> {
	@Override
	public DeviceDto dtoParser(Device device) {
		DeviceDto deviceDto = new DeviceDto();
		deviceDto.setId(deviceDto.getId());
		deviceDto.setDeviceId(deviceDto.getDeviceId());
		deviceDto.setName(deviceDto.getName());
		return deviceDto;
	}

	@Override
	public Device modelParser(DeviceDto deviceDto) {
		Device device = new Device();
		device.setId(deviceDto.getId());
		device.setDeviceId(deviceDto.getDeviceId());
		device.setName(deviceDto.getName());
		return device;
	}
}
