package com.freela.service.parameter;

import com.freela.database.enums.Role;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

//TODO Bring those request parameters to service folder
@Introspected
public class ApiUserSearchRequest {
	List<Role> roles;
	String name;
	String email;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
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
