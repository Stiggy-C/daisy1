package io.openenterprise.daisy.service;

import io.openenterprise.daisy.InvocationContext;
import io.openenterprise.daisy.domain.Action;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public interface ActionService {

    @Nullable
    <T> T invoke(@Nonnull InvocationContext invocationContext, @Nonnull Action<T> action);

    @Nullable
    <T> T invoke(@Nonnull UUID invocationContextId, @Nonnull Action<T> action);

    @Nullable
    Object invoke(@Nonnull InvocationContext invocationContext, @Nonnull List<Action<?>> actions);

    @Nullable
    Object invoke(@Nonnull UUID invocationContextId, @Nonnull List<Action<?>> actions);
}
