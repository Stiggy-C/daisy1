package io.openenterprise.daisy.spark;

import jakarta.annotation.Nonnull;

public enum Parameter implements io.openenterprise.daisy.Parameter {

    SQL_STATEMENTS("daisy.spark.sql-queries", String[].class)
    
    ;

    private final String key;

    private final Class<?> valueTYpe;

    Parameter(@Nonnull String key, @Nonnull Class<?> valueType) {
        this.key = key;
        this.valueTYpe = valueType;
    }

    @Nonnull
    @Override
    public String getKey() {
        return key;
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getValueType() {
        return (Class<T>) valueTYpe;
    }
}
