package com.freela.database.model;

import io.micronaut.core.util.StringUtils;
import io.micronaut.data.annotation.DateCreated;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(indexes = {
		@Index(name = "refresh_token_value", columnList = "value"),
		@Index(name = "refresh_token_expiration_time", columnList = "expiration"),
})
public class RefreshToken implements ModelInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
	@SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
	private Long id;

	@Column(length = 2000, nullable = false)
	private String value;

	@ManyToOne(optional = false)
	private Device device;

	@ManyToOne(optional = false)
	private ApiUser apiUser;

	@Column(nullable = false)
	private Boolean blocked = false;

	@DateCreated
	@Column(nullable = false, columnDefinition = "timestamp with time zone")
	private OffsetDateTime creation;

	@Column(nullable = false, columnDefinition = "timestamp with time zone")
	private OffsetDateTime expiration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public ApiUser getApiUser() {
		return apiUser;
	}

	public void setApiUser(ApiUser apiUser) {
		this.apiUser = apiUser;
	}

	public OffsetDateTime getCreation() {
		return creation;
	}

	public void setCreation(OffsetDateTime creation) {
		this.creation = creation;
	}

	public OffsetDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(OffsetDateTime expiration) {
		this.expiration = expiration;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public String toString() {
		return "RefreshToken{" +
				"id=" + id +
				", isValuePresent='" + (StringUtils.isEmpty(value) ? "true" : "false") + '\'' +
				", device=" + device +
				", apiUser=" + apiUser +
				", blocked=" + blocked +
				", creation=" + creation +
				", expiration=" + expiration +
				'}';
	}
}
