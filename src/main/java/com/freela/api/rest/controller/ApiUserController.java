package com.freela.api.rest.controller;

import com.freela.api.dto.ApiUserDto;
import com.freela.api.dto.ErrorDto;
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
import io.micronaut.core.version.annotation.Version;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

@Transactional
@Version("1")
@Controller("/api-users")
@ExecuteOn(TaskExecutors.IO)
public class ApiUserController {

	private static final Logger log = LoggerFactory.getLogger(ApiUserController.class);

	@Inject
	ApiUserParser apiUserParser;

	@Inject
	ApiUserService apiUserService;

	@Inject
	AuthenticationUtils authenticationUtils;

	@Get("/{apiUserId}")
	@Secured({"API_USER_GET"})
	@Operation(operationId = "getApiUser")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	@ApiResponse(responseCode = "404",
			description = "User not found",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<ApiUserDto> get(Long apiUserId) {
		log.info("get: { apiUserId: {} }", apiUserId);
		Optional<ApiUser> apiUser = apiUserService.getById(apiUserId);

		if (apiUser.isEmpty()) {
			throw new NotFoundException("API user not found", new ApiException.Source(
					ApiException.Location.PATH,
					"apiUserId",
					String.format("%s", apiUserId),
					"Valid API User ID"));
		}

		ApiUserDto apiUserDto = apiUserParser.toDto(apiUser.get());
		return HttpResponse.ok(apiUserDto);
	}

	@Get("/self")
	@Secured({"API_USER_GET_SELF"})
	@Operation(operationId = "getApiUserSelf")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	@ApiResponse(responseCode = "404",
			description = "User not found",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<ApiUserDto> getSelf(Authentication authentication) {
		Long apiUserId = authenticationUtils.getApiUserId(authentication);
		log.info("getSelf: { apiUserId: {} }", apiUserId);
		Optional<ApiUser> apiUser = apiUserService.getById(apiUserId);

		if (apiUser.isEmpty()) {
			throw new NotFoundException("API user not found", new ApiException.Source(
					ApiException.Location.PATH,
					"apiUserId",
					String.format("%s", apiUserId),
					"Valid API User ID"));
		}

		ApiUserDto apiUserDto = apiUserParser.toDto(apiUser.get());
		return HttpResponse.ok(apiUserDto);
	}

	@Put("/{apiUserId}")
	@Secured({"API_USER_UPDATE"})
	@Operation(operationId = "updateApiUser")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<ApiUserDto> update(
			@Body @Valid ApiUserDto apiUserDto,
			Long apiUserId
	) {
		log.info("update: { apiUserDto: {}, apiUserId: {} }", apiUserDto, apiUserId);
		ApiUser apiUser = apiUserParser.toModel(apiUserDto);
		apiUserService.update(
				apiUserId,
				apiUser);
		return HttpResponse.ok(apiUserDto);
	}

	@Put("/self")
	@Secured({"API_USER_UPDATE_SELF"})
	@Operation(operationId = "updateApiUserSelf")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<ApiUserDto> updateSelf(
			@Body @Valid ApiUserDto apiUserDto,
			Authentication authentication
	) {
		Long apiUserId = authenticationUtils.getApiUserId(authentication);
		log.info("updateSelf: { apiUserDto: {}, apiUserId: {} }", apiUserDto, apiUserId);
		ApiUser apiUser = apiUserParser.toModel(apiUserDto);
		apiUserService.update(
				apiUserId,
				apiUser);
		return HttpResponse.ok(apiUserDto);
	}

	@Post("/validate")
	@PermitAll
	@Operation(operationId = "validateApiUser")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<Void> validate(@Body @Valid ValidateRequest validateRequest) throws InvalidKeySpecException {
		log.info("validate: { validateRequest: {} }", validateRequest);
		apiUserService.validateApiUser(validateRequest.getRecoveryCode(), validateRequest.getPassword());
		return HttpResponse.ok();
	}

	@Get("/forgot-password")
	@PermitAll
	@Operation(operationId = "forgotPassword")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<Void> forgotPassword(@QueryValue(value = "email") String email) {
		log.info("forgotPassword: { email: {} }", email);
		apiUserService.forgotPassword(email);
		return HttpResponse.ok();
	}

	@Post
	@PermitAll
	@Operation(operationId = "createApiUser")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<ApiUserDto> create(@Body @Valid ApiUserDto apiUserDto) throws InvalidKeySpecException {
		log.info("create: { apiUserDto: {} }", apiUserDto);
		ApiUser contact = apiUserService.create(apiUserParser.toModel(apiUserDto), apiUserDto.getPassword());
		return HttpResponse.ok(apiUserParser.toDto(contact));
	}

	@Get("/list{?pageRequest*}")
	@Secured({"API_USER_LIST"})
	@Operation(operationId = "listApiUsers")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<PageDto<ApiUserDto>> list(PageRequest pageRequest) {
		log.info("list: { pageRequest: {} }", pageRequest);
		if(pageRequest == null) {
			pageRequest = new PageRequest();
		}

		Page<ApiUser> contacts = apiUserService.findAll(pageRequest);
		return HttpResponse.ok(apiUserParser.toPagedDto(contacts));
	}

	@Get("/search{?pageRequest*}{?apiUserSearchRequest*}")
	@Secured({"API_USER_SEARCH"})
	@Operation(operationId = "searchApiUsers")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<PageDto<ApiUserDto>> search(
			PageRequest pageRequest,
			ApiUserSearchRequest apiUserSearchRequest
	) {
		log.info("search: { pageRequest: {}, apiUserSearchRequest: {} }", pageRequest, apiUserSearchRequest);
		if(pageRequest == null)
			pageRequest = new PageRequest();

		Page<ApiUser> contacts = apiUserService.search(
				pageRequest,
				apiUserSearchRequest);
		return HttpResponse.ok(apiUserParser.toPagedDto(contacts));
	}
}
