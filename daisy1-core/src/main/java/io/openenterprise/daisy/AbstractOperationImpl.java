package io.openenterprise.daisy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractOperationImpl<T> implements Operation<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationImpl.class);

    protected void preInvoke(@Nonnull InvocationContext invocationContext,
                             @Nonnull Invocation<? extends AbstractOperationImpl<T>, T> invocation,
                             @Nonnull Parameters parameters) {
        LOGGER.debug("{}.preInvoke invoked", this.getClass());
    }

    protected void postInvoke(@Nonnull InvocationContext invocationContext,
                              @Nonnull Invocation<? extends AbstractOperationImpl<T>, T> invocation,
                              @Nullable T result) {
        LOGGER.debug("{}.postInvoke invoked", this.getClass());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <S> S readParameterValue(@Nonnull Parameters parameters, @Nonnull Parameter parameter) {
        return Optional.ofNullable(parameters.get(parameter))
                .map(param -> ((Class<S>) parameter.getValueType()).cast(param))
                .orElse(null);
    }
}
