package com.freela.api.dto;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Introspected
@Replaces(bean = UsernamePasswordCredentials.class)
@Schema(name="Credentials", description="Necessary information for authentication")
public class CredentialsDto extends UsernamePasswordCredentials {
	@NotNull
	private DeviceDto device;

	public DeviceDto getDevice() {
		return device;
	}

	public void setDevice(DeviceDto device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "CustomUsernamePasswordCredentials{" +
				"username='" + getIdentity() + "'," +
				"device='" + device + '\'' +
				'}';
	}
}
