package io.openenterprise.daisy;

import jakarta.annotation.Nonnull;

public interface Parameter {

    @Nonnull
    String getKey();

    @Nonnull
    <T> Class<T> getValueType();
}
