package io.openenterprise.daisy;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface Operation<T> {

    @Nullable
    T invoke(@Nonnull Invocation<? extends Operation<T>, T> invocation);

    void preInvoke(@Nonnull Invocation<? extends Operation<T>, T> invocation);

    void postInvoke(@Nonnull Invocation<? extends Operation<T>, T> invocation, @Nullable T result);

}
