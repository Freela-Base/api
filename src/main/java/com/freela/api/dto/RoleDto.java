package com.freela.api.dto;

import com.freela.database.enums.ApiAction;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Introspected
@Schema(name="Role", description="API Role information")
public class RoleDto implements DtoInterface {
	Long id;

	@NotBlank
	String name;

	@NotNull
	Set<ApiAction> apiActions;

	Boolean deleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NonNull
	public String getName() {
		return name;
	}

	public void setName(@NonNull String name) {
		this.name = name;
	}

	@NonNull
	public Set<ApiAction> getApiActions() {
		return apiActions;
	}

	public void setApiActions(@NonNull Set<ApiAction> apiActions) {
		this.apiActions = apiActions;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "RoleDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				", apiActions=" + apiActions +
				", deleted=" + deleted +
				'}';
	}
}
