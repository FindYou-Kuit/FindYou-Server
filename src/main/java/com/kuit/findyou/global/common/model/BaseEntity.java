package com.kuit.findyou.global.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.kuit.findyou.global.common.model.BaseStatus.*;
import static com.kuit.findyou.global.common.model.BaseStatus.N;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1)", nullable = false)
    private BaseStatus status = N;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
