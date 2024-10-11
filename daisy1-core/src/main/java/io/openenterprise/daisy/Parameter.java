package io.openenterprise.daisy;

import javax.annotation.Nonnull;

public interface Parameter {

    @Nonnull
    String getKey();

    @Nonnull
    <T> Class<T> getValueType();

}
