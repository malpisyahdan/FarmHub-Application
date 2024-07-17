package com.project.app.farmhub.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
@Getter
@Setter
public abstract class MasterEntity {

	@Id
	@Column(name = "id", nullable = false)
	@UuidGenerator
	private String id;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Column(name = "version")
	@Version
	private Long version;

}
