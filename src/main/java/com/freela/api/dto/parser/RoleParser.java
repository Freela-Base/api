package com.freela.api.dto.parser;

import com.freela.api.dto.RoleDto;
import com.freela.database.model.Role;
import jakarta.inject.Singleton;

@Singleton
public class RoleParser implements ParserInterface<Role, RoleDto> {
	@Override
	public RoleDto dtoParser(Role role) {
		RoleDto roleDto = new RoleDto();
		roleDto.setId(role.getId());
		roleDto.setName(role.getName());
		roleDto.setApiActions(role.getApiActions());
		roleDto.setDeleted(role.getDeleted());
		return roleDto;
	}

	@Override
	public Role modelParser(RoleDto roleDto) {
		Role role = new Role();
		role.setId(roleDto.getId());
		role.setName(roleDto.getName());
		role.setApiActions(roleDto.getApiActions());
		return role;
	}
}
