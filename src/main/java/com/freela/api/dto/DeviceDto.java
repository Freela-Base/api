package com.freela.api.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Introspected
@Schema(name="Device", description="Device information")
public class DeviceDto implements DtoInterface {
	private Long id;
	@NotBlank
	private String deviceId;
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DeviceDto{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
