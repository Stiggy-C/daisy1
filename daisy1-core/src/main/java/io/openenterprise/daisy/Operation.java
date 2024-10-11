package io.openenterprise.daisy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Operation<T> {

    @Nullable
    T invoke(@Nonnull Invocation<?, T> invocation);

}
