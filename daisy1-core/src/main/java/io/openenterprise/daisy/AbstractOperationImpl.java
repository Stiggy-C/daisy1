package io.openenterprise.daisy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractOperationImpl<T> implements Operation<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationImpl.class);

    @Override
    public void preInvoke(@Nonnull Invocation<? extends Operation<T>, T> invocation) {
        preInvoke(invocation.invocationContext, invocation, invocation.parameters);
    }

    @Override
    public void postInvoke(@Nonnull Invocation<? extends Operation<T>, T> invocation, @Nullable T result) {
        postInvoke(invocation.invocationContext, invocation, result);
    }

    protected void preInvoke(@Nonnull InvocationContext invocationContext,
                             @Nonnull Invocation<? extends Operation<T>, T> invocation,
                             @Nonnull Map<io.openenterprise.daisy.Parameter, Object> parameters) {
        LOGGER.debug("{}.preInvoke invoked", this.getClass());
    }

    public void postInvoke(@Nonnull InvocationContext invocationContext,
                              @Nonnull Invocation<? extends Operation<T>, T> invocation,
                              @Nullable T result) {
        LOGGER.debug("{}.postInvoke invoked", this.getClass());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <S> S readParameterValue(@Nonnull Map<Parameter, Object> parameters, @Nonnull Parameter parameter) {
        return Optional.ofNullable(parameters.get(parameter))
                .map(param -> ((Class<S>) parameter.getValueType()).cast(param))
                .orElse(null);
    }
}
