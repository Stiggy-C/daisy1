package io.openenterprise.graalvm.polyglot;

import org.graalvm.polyglot.Value;

import jakarta.annotation.Nonnull;

public interface ContextDelegate extends AutoCloseable {

    void enter();

    @Nonnull
    Value eval(@Nonnull String source);

    default <T> T evalToJavaType(@Nonnull String source, @Nonnull Class<T> clazz) {
        var value = eval(source);

        if (value.isHostObject()) {
            return value.as(clazz);
        } else {
            throw new UnsupportedOperationException("Value returned can not be mapped to given Java type, " + clazz);
        }
    }
}
