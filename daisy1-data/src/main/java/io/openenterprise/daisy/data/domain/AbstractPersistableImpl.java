package io.openenterprise.daisy.data.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.Objects;

@Data
public abstract class AbstractPersistableImpl<I extends Serializable, U, T extends Temporal>
        implements Persistable<I, U, T> {

    protected I id;

    protected U createdBy;

    protected T createdInstant;

    @Override
    public boolean isNew() {
        return Objects.nonNull(id);
    }
}
