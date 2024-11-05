package io.openenterprise.graalvm.polyglot;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContextDelegateImpl implements ContextDelegate {

    protected Context context;

    protected String supportedLanguage;

    public ContextDelegateImpl(@Nonnull Context context, @Nonnull String supportedLanguage) {
        this.context = context;
        this.supportedLanguage = supportedLanguage;
    }

    @Override
    public void enter() {
        this.context.enter();
    }

    @Nonnull
    @Override
    public Value eval(@Nonnull String source) {
        return context.eval(supportedLanguage, source);
    }

    @Nullable
    @Override
    public <T> T evalToJavaType(@Nonnull String source, @Nonnull Class<T> clazz) {
        var value = eval(source);

        if (value.isHostObject()) {
            return value.as(clazz);
        } else {
            throw new UnsupportedOperationException("Value returned can not be mapped to given Java type, " + clazz);
        }
    }

    @Override
    public void close() {
        this.context.leave();
    }
}
