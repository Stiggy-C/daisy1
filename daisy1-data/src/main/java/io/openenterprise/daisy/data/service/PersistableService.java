package io.openenterprise.daisy.data.service;

import io.openenterprise.daisy.data.domain.Persistable;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;

public interface PersistableService<A extends Persistable<I, U, T>, I extends Serializable, U, T extends Temporal> {

    void delete(@Nonnull A auditable);

    void deleteById(@Nonnull I id);

    boolean existsById(@Nonnull I id);

    @Nonnull
    List<A> findByCreatedBy(@Nonnull U createdBy);

    @Nullable
    A findById(@Nonnull I id);

    @Nonnull
    A save(@Nonnull A auditable);

}