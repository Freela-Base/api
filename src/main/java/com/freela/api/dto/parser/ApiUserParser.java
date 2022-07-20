package com.freela.api.dto.parser;

import com.freela.api.dto.ApiUserDto;
import com.freela.database.model.ApiUser;
import com.freela.exception.ApiException;
import com.freela.utils.DateTimeUtils;
import com.freela.utils.RoleUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ApiUserParser implements ParserInterface<ApiUser, ApiUserDto> {

	@Inject
	DateTimeUtils dateTimeUtils;

	@Inject
	RoleUtils roleUtils;

	@Override
	public ApiUserDto dtoParser(ApiUser apiUser) {
		ApiUserDto apiUserDto = new ApiUserDto();
		apiUserDto.setId(apiUser.getId());
		apiUserDto.setName(apiUser.getName());
		apiUserDto.setEmail(apiUser.getEmail());
		apiUserDto.setPhoneNumber(apiUser.getPhoneNumber());
		apiUserDto.setBirthDate(dateTimeUtils.convertToDate(apiUser.getBirthDate()));
		apiUserDto.setRoles(roleUtils.getRoles(apiUser.getRoles()));
		apiUserDto.setApiActions(roleUtils.getStrApiActions(apiUser.getRoles()));

		return apiUserDto;
	}

	@Override
	public ApiUser modelParser(ApiUserDto contactDTO) {
		ApiUser contact = new ApiUser();
		contact.setId(contactDTO.getId());
		contact.setName(contactDTO.getName());
		contact.setEmail(contactDTO.getEmail());
		contact.setPhoneNumber(contactDTO.getPhoneNumber());
		contact.setBirthDate(dateTimeUtils.convertToDate(contactDTO.getBirthDate(), new ApiException.Source(
				ApiException.Location.BODY,
				"contact",
				contactDTO.getBirthDate(),
				"Valid Date"
		)));

		return contact;
	}
}
