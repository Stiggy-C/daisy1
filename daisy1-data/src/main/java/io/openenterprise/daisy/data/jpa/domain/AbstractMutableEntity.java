package io.openenterprise.daisy.data.jpa.domain;

import io.openenterprise.daisy.data.domain.AbstractAuditableImpl;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.temporal.Temporal;

@MappedSuperclass
public abstract class AbstractMutableEntity<I extends Serializable, U, T extends Temporal>
        extends AbstractAuditableImpl<I, U, T> {

    @Access(AccessType.PROPERTY)
    @Override
    public U getUpdatedBy() {
        return super.getUpdatedBy();
    }

    @Access(AccessType.PROPERTY)
    @LastModifiedDate
    @Override
    public T getUpdatedInstant() {
        return super.getUpdatedInstant();
    }

    @Access(AccessType.PROPERTY)
    @Override
    public long getVersion() {
        return super.getVersion();
    }

    @Access(AccessType.PROPERTY)
    @Override
    public U getCreatedBy() {
        return super.getCreatedBy();
    }

    @Access(AccessType.PROPERTY)
    @CreatedDate
    @Override
    public T getCreatedInstant() {
        return super.getCreatedInstant();
    }
}
