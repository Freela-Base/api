package com.freela.database.model;

import com.freela.database.enums.ApiAction;
import io.micronaut.data.annotation.DateCreated;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(indexes = {
		@Index(name = "role_name_index", columnList = "name"),
})
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
	@SequenceGenerator(name = "role_seq", allocationSize = 1)
	private Long id;

	@Column(length = 500, nullable = false, unique = true)
	private String name;

	//TODO every time hibernate creates this table
	// it also creates a useless column "api_actions" in table "role"
	@CollectionTable(
			name = "role_api_action",
			joinColumns = @JoinColumn(name = "role_id"))
	@ElementCollection(targetClass = ApiAction.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private Set<ApiAction> apiActions;

	@DateCreated
	@Column(nullable = false, columnDefinition = "timestamp with time zone")
	private OffsetDateTime creation;

	@Column(nullable = false)
	private Boolean deleted = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ApiAction> getApiActions() {
		return apiActions;
	}

	public void setApiActions(Set<ApiAction> apiActions) {
		this.apiActions = apiActions;
	}

	public OffsetDateTime getCreation() {
		return creation;
	}

	public void setCreation(OffsetDateTime creation) {
		this.creation = creation;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", name='" + name + '\'' +
				", apiActions=" + apiActions +
				", creation=" + creation +
				'}';
	}
}
