package com.freela.api.model.parser;

import com.freela.api.model.ApiUserDto;
import com.freela.database.enums.Role;
import com.freela.database.model.ApiUser;
import com.freela.exception.ApiException;
import com.freela.utils.DateTimeUtils;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

public class ApiUserParser implements ParserInterface<ApiUser, ApiUserDto>{

	@Inject
	DateTimeUtils dateTimeUtils;

	@Override
	public ApiUserDto toDto(ApiUser apiUser) {
		if(apiUser == null) {
			return null;
		}

		ApiUserDto apiUserDto = new ApiUserDto();
		apiUserDto.setId(apiUser.getId());
		if(org.hibernate.Hibernate.isInitialized(apiUser)) {
			apiUserDto.setName(apiUser.getName());
			apiUserDto.setEmail(apiUser.getEmail());
			apiUserDto.setPhoneNumber(apiUser.getPhoneNumber());
			apiUserDto.setBirthDate(dateTimeUtils.convertToDate(apiUser.getBirthDate()));

			if(apiUser.getRoles() != null) {
				apiUserDto.setRoles(apiUser.getRoles().stream()
						.map(Role::name)
						.collect(Collectors.toList()));
			}
		}

		return apiUserDto;
	}

	@Override
	public ApiUser toModel(ApiUserDto contactDTO) {
		if(contactDTO == null) {
			return null;
		}

		ApiUser contact = new ApiUser();
		contact.setId(contactDTO.getId());
		contact.setName(contactDTO.getName());
		contact.setEmail(contactDTO.getEmail());
		contact.setPhoneNumber(contactDTO.getPhoneNumber());
		contact.setBirthDate(dateTimeUtils.convertToDate(contactDTO.getBirthDate(), new ApiException.Source(
				ApiException.Location.BODY,
				"",
				"contact",
				contactDTO.getBirthDate(),
				"Valid Date"
		)));

		return contact;
	}
}
