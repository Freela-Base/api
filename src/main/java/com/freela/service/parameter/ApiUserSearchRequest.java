package com.freela.service.parameter;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class ApiUserSearchRequest {
	List<String> roles;
	String name;
	String email;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ApiUserSearchRequest{" +
				"roles=" + roles +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
