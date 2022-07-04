package com.freela.service.parameter;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.util.StringUtils;

import javax.validation.constraints.NotNull;

//TODO Bring those request parameters to service folder
@Introspected
public class ValidateRequest {
	@NotNull
	String recoveryCode;
	String password;

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ValidateRequest{" +
				"token='" + recoveryCode + '\'' +
				", password='" + StringUtils.isNotEmpty(password) + '\'' +
				'}';
	}
}
