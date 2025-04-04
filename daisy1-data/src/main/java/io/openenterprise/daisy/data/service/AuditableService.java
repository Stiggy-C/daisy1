package io.openenterprise.daisy.data.service;

import io.openenterprise.daisy.data.domain.Auditable;
import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;

public interface AuditableService<A extends Auditable<I, U, T>, I extends Serializable, U, T extends Temporal>
    extends PersistableService<A, I, U, T> {

    @Nonnull
    List<A> findByUpdatedBy(@Nonnull U updatedByß);

}
