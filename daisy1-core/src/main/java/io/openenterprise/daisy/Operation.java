package io.openenterprise.daisy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Operation<T> {

    @Nullable
    T invoke(@Nonnull Invocation<? extends Operation<T>, T> invocation);

    void preInvoke(@Nonnull Invocation<? extends Operation<T>, T> invocation);

    void postInvoke(@Nonnull Invocation<? extends Operation<T>, T> invocation, @Nullable T result);

}
