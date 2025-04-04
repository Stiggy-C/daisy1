package io.openenterprise.daisy.data.jpa.domain;

import io.openenterprise.daisy.data.domain.AbstractPersistableImpl;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.temporal.Temporal;

public abstract class AbstractEntity<I extends Serializable, U, T extends Temporal>
        extends AbstractPersistableImpl<I, U, T> {

    @Access(AccessType.PROPERTY)
    @Override
    public I getId() {
        return super.getId();
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
