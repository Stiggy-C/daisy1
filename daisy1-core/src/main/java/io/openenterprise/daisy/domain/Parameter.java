package io.openenterprise.daisy.domain;

import javax.annotation.Nonnull;

public enum Parameter implements io.openenterprise.daisy.Parameter {

    MVEL_CLASS_IMPORTS("daisy.mvel2.class-imports", String[].class),

    MVEL_EXPRESSIONS("daisy.mvel2.expressions", String[].class),

    MVEL_PACKAGE_IMPORTS("daisy.mvel2.package-imports", String[].class)

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
