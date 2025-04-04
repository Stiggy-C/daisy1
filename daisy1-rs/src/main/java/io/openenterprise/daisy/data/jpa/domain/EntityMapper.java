package io.openenterprise.daisy.data.jpa.domain;

import io.openenterprise.daisy.data.domain.Auditable;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.time.temporal.Temporal;

public interface EntityMapper<A extends Auditable<? extends Serializable, ? extends Serializable, ? extends Temporal>, M> {

    @Nullable
    M entityToRestModel(@Nullable A entity);

    @Nullable
    A restModelToEntity(@Nullable M model);
}
