package com.freela.api.rest.controller;

import com.freela.api.dto.ErrorDto;
import com.freela.api.dto.PageDto;
import com.freela.api.dto.RoleDto;
import com.freela.api.dto.parser.RoleParser;
import com.freela.api.utils.AuthenticationUtils;
import com.freela.database.model.Role;
import com.freela.service.RoleService;
import com.freela.service.parameter.PageRequest;
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
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Transactional
@Version("1")
@Controller("/roles")
@ExecuteOn(TaskExecutors.IO)
public class RoleController {
	public static final Logger log = LoggerFactory.getLogger(RoleController.class);

	@Inject
	RoleService roleService;

	@Inject
	RoleParser roleParser;

	@Inject
	AuthenticationUtils authenticationUtils;

	@Post
	@Secured({"ROLE_CREATE"})
	@Operation(operationId = "createRole")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<RoleDto> create(
			@Body @Valid RoleDto role,
			Authentication authentication
	) {
		log.info("update: { role: {} }", role);
		Role updatedRole = roleService.create(
				roleParser.toModel(role), authenticationUtils.getApiActions(authentication));
		return HttpResponse.ok(roleParser.toDto(updatedRole));
	}

	@Put("/{roleId}")
	@Secured({"ROLE_UPDATE"})
	@Operation(operationId = "updateRole")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<RoleDto> update(
			@Body @Valid RoleDto role,
			Long roleId,
			Authentication authentication
	) {
		log.info("update: { roleId: {}, role: {} }", roleId, role);
		Role updatedRole = roleService.update(
				roleId, roleParser.toModel(role), authenticationUtils.getApiActions(authentication));
		return HttpResponse.ok(roleParser.toDto(updatedRole));
	}

	@Delete("/{roleId}")
	@Secured({"ROLE_DELETE"})
	@Operation(operationId = "deleteRole")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<RoleDto> delete(
			Long roleId,
			Authentication authentication
	) {
		log.info("delete: { roleId: {} }", roleId);
		roleService.delete(
				roleId, authenticationUtils.getApiActions(authentication));
		return HttpResponse.ok();
	}

	@Get("{?pageRequest*}")
	@Secured({"ROLE_LIST"})
	@Operation(operationId = "listRoles")
	@ApiResponse(responseCode = "200", description = "Successful operation")
	@ApiResponse(responseCode = "400",
			description = "Unsuccessful  operation",
			content = @Content(schema = @Schema(implementation = ErrorDto.class)))
	public HttpResponse<PageDto<RoleDto>> list(PageRequest pageRequest) {
		log.info("list: { pageRequest: {} }", pageRequest);

		if (pageRequest == null) {
			pageRequest = new PageRequest();
		}

		Page<Role> roles = roleService.findAll(pageRequest);
		return HttpResponse.ok(roleParser.toPagedDto(roles));
	}
}
