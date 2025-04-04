package io.openenterprise.daisy.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.temporal.Temporal;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuditableImpl<I extends Serializable, U, T extends Temporal>
        extends AbstractPersistableImpl<I, U, T> implements Auditable<I, U, T> {

    protected U updatedBy;

    protected T updatedInstant;

    protected long version;

}
