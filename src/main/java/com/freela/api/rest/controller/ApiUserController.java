package com.freela.api.rest.controller;

import com.freela.api.dto.ApiUserDto;
import com.freela.api.dto.PageDto;
import com.freela.api.dto.parser.ApiUserParser;
import com.freela.api.utils.AuthenticationUtils;
import com.freela.database.model.ApiUser;
import com.freela.exception.ApiException;
import com.freela.exception.NotFoundException;
import com.freela.service.ApiUserService;
import com.freela.service.parameter.ApiUserSearchRequest;
import com.freela.service.parameter.PageRequest;
import com.freela.service.parameter.ValidateRequest;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api-users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ApiUserController {

	private static final Logger log = LoggerFactory.getLogger(ApiUserController.class);

	@Inject
	ApiUserParser apiUserParser;

	@Inject
	ApiUserService apiUserService;

	@Inject
	AuthenticationUtils authenticationUtils;

	@Get("/{apiUserId}")
	public HttpResponse<ApiUserDto> get(Long apiUserId, Authentication authentication) {
		log.info("get: { apiUserId: {} }", apiUserId);
		Optional<ApiUser> apiUser = apiUserService.getById(
				apiUserId,
				authenticationUtils.getApiUserId(authentication),
				authenticationUtils.getRoles(authentication));

		if (apiUser.isEmpty()) {
			throw new NotFoundException("API user not found", new ApiException.Source(
					ApiException.Location.PATH,
					"apiUserId",
					String.format("%s", apiUserId),
					"Valid API User ID"
			));
		}

		ApiUserDto apiUserDto = apiUserParser.toDto(apiUser.get());
		return HttpResponse.ok(apiUserDto);
	}

	@Put("/{apiUserId}")
	public HttpResponse<ApiUserDto> update(
			@Body @Valid ApiUserDto apiUserDto,
			Long apiUserId,
			Authentication authentication
	) {
		log.info("update: { apiUserDto: {}, apiUserId: {} }", apiUserDto, apiUserId);
		ApiUser apiUser = apiUserParser.toModel(apiUserDto);
		apiUserService.update(
				apiUserId,
				apiUser,
				authenticationUtils.getApiUserId(authentication),
				authenticationUtils.getRoles(authentication)
		);
		return HttpResponse.ok(apiUserDto);
	}

	@Post("/validate")
	@PermitAll
	public HttpResponse<?> validate(@Body @Valid ValidateRequest validateRequest) throws InvalidKeySpecException {
		log.info("validate: { validateRequest: {} }", validateRequest);
		apiUserService.validateApiUser(validateRequest.getRecoveryCode(), validateRequest.getPassword());
		return HttpResponse.ok();
	}

	@Get("/forgot-password")
	@PermitAll
	public HttpResponse<?> forgotPassword(@QueryValue(value = "email") String email) {
		log.info("forgotPassword: { email: {} }", email);
		apiUserService.forgotPassword(email);
		return HttpResponse.ok();
	}

	@Post
	@PermitAll
	public HttpResponse<ApiUserDto> create(@Body @Valid ApiUserDto apiUserDto) throws InvalidKeySpecException {
		log.info("create: { apiUserDto: {} }", apiUserDto);
		ApiUser contact = apiUserService.create(apiUserParser.toModel(apiUserDto), apiUserDto.getPassword());
		return HttpResponse.ok(apiUserParser.toDto(contact));
	}

	@Get("/list{?pageRequest*}")
	@Secured({"ADMIN"})
	public HttpResponse<PageDto<ApiUserDto>> list(PageRequest pageRequest) {
		log.info("list: { pageRequest: {} }", pageRequest);
		if(pageRequest == null)
			pageRequest = new PageRequest();

		Page<ApiUser> contacts = apiUserService.findAll(pageRequest);
		return HttpResponse.ok(apiUserParser.toPagedDto(contacts));
	}

	@Get("/search{?pageRequest*}{?apiUserSearchRequest*}")
	public HttpResponse<PageDto<ApiUserDto>> search(
			PageRequest pageRequest,
			ApiUserSearchRequest apiUserSearchRequest,
			Authentication authentication
	) {
		log.info("search: { pageRequest: {}, apiUserSearchRequest: {} }", pageRequest, apiUserSearchRequest);
		if(pageRequest == null)
			pageRequest = new PageRequest();

		Page<ApiUser> contacts = apiUserService.search(
				pageRequest,
				apiUserSearchRequest,
				authenticationUtils.getRoles(authentication)
		);
		return HttpResponse.ok(apiUserParser.toPagedDto(contacts));
	}
}
