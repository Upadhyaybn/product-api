package com.bhawani.product_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Common parent class containing audit and soft-delete fields.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Automatically populated when the record is created.
     */
    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * Automatically updated whenever the record changes.
     */
    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    /**
     * Indicates whether the record has been soft deleted.
     *
     * false = normal record
     * true  = deleted record
     */
    @Column(
            name = "deleted",
            nullable = false
    )
    private Boolean deleted = false;

    /**
     * Stores the date and time when the record was deleted.
     *
     * It remains null until soft deletion occurs.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}