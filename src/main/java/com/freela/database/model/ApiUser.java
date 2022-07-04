package com.freela.database.model;

import com.freela.database.enums.Role;
import io.micronaut.data.annotation.DateCreated;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "api_user", indexes = {
		@Index(name = "api_user_name_index", columnList = "name"),
		@Index(name = "api_user_email_index", columnList = "email"),
		@Index(name = "api_user_phone_number_index", columnList = "phoneNumber"),
		@Index(name = "api_user_recovery_code_index", columnList = "recoveryCode"),
})
public class ApiUser implements ModelInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(length = 500, nullable = false)
	private String name;

	@Column(length = 100, nullable = false)
	private String phoneNumber;

	@Column(length = 500, nullable = false, unique = true)
	private String email;

	@Column(length = 512, nullable = true, unique = true)
	private String passwordSalt;

	@Column(length = 512, nullable = true, unique = true)
	private String passwordPepper;

	@Column(length = 2048, nullable = true)
	private String passwordHash;

	@Column(nullable = true)
	private LocalDate birthDate;

	@Column(nullable = false)
	private Boolean validated = false;

	@Column(nullable = false)
	private Boolean deleted = false;

	@DateCreated
	@Column(nullable = false, columnDefinition = "timestamp with time zone")
	private OffsetDateTime creationDateTime;

	@Column(length = 512, nullable = true)
	private String recoveryCode;

	@Column(nullable = true)
	private OffsetDateTime recoveryCodeValidUntil;


	//TODO every time hibernate creates this table
	// it also creates a useless column "roles" in table api_user
	@CollectionTable(
			name = "api_user_role",
			joinColumns = @JoinColumn(name = "api_user_id"))
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = true)
	private Set<Role> roles;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Boolean isValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public OffsetDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(OffsetDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public OffsetDateTime getRecoveryCodeValidUntil() {
		return recoveryCodeValidUntil;
	}

	public void setRecoveryCodeValidUntil(OffsetDateTime passwordRecoveryCodeValidUntilMillis) {
		this.recoveryCodeValidUntil = passwordRecoveryCodeValidUntilMillis;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordPepper() {
		return passwordPepper;
	}

	public void setPasswordPepper(String passwordPepper) {
		this.passwordPepper = passwordPepper;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "ApiUser{" +
				"id=" + id +
				", name='" + name + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", email='" + email + '\'' +
				", passwordSalt='" + passwordSalt + '\'' +
				", passwordPepper='" + passwordPepper + '\'' +
				", passwordHash='" + passwordHash + '\'' +
				", birthDate=" + birthDate +
				", validated=" + validated +
				", deleted=" + deleted +
				", creationDateTime=" + creationDateTime +
				", recoveryCode='" + recoveryCode + '\'' +
				", recoveryCodeValidUntil=" + recoveryCodeValidUntil +
				", roles=" + roles +
				'}';
	}
}
