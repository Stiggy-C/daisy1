package io.openenterprise.daisy.python;

import jakarta.annotation.Nonnull;

public enum Parameter implements io.openenterprise.daisy.Parameter {

    PYTHON_PRE_EVAL_SOURCE("daisy.python.pre-eval.source", String[].class),

    PYTHON_SCRIPT_URI("daisy.python.script.uri", String.class),

    PYTHON_SOURCE("daisy.python.source", String.class)

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
