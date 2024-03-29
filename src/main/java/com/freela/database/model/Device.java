package com.freela.database.model;

import io.micronaut.data.annotation.DateCreated;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Device implements ModelInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
	@SequenceGenerator(name = "device_seq", allocationSize = 1)
	private Long id;

	@Column(length = 500, nullable = false, unique = true)
	private String deviceId;

	@Column(length = 500, nullable = true)
	private String name;

	@DateCreated
	@Column(nullable = false, columnDefinition = "timestamp with time zone")
	private OffsetDateTime creation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OffsetDateTime getCreation() {
		return creation;
	}

	public void setCreation(OffsetDateTime creation) {
		this.creation = creation;
	}

	@Override
	public String toString() {
		return "Device{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", name='" + name + '\'' +
				", creation=" + creation +
				'}';
	}
}
