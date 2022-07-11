package com.freela.api.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

@Introspected
public class DeviceDto implements DtoInterface {
	private Long id;
	@NotBlank
	private String deviceId;
	private String deviceName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public String toString() {
		return "DeviceDto{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", deviceName='" + deviceName + '\'' +
				'}';
	}
}
