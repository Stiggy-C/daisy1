package io.openenterprise.daisy;

import io.openenterprise.commons.util.UUIDUtils;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
public class Invocation<T extends Operation<S>, S> implements Serializable {

    public Invocation() {
        id = UUIDUtils.randomUUIDv7();
        createdDateTime = OffsetDateTime.now();
    }

    protected UUID id;

    @Setter
    protected OffsetDateTime completionDateTime;

    @Setter
    protected OffsetDateTime createdDateTime;

    @Setter
    protected InvocationContext invocationContext;

    @Setter
    protected OffsetDateTime invocationDateTime;

    @Setter
    protected T operation;

    @Setter
    protected Map<Parameter, Object> parameters;

    @Setter
    protected S result;

    @Setter
    protected Throwable throwable;

    public static class Builder<T extends AbstractOperationImpl<S>, S> {

        private final Invocation<T, S> invocation;

        public Builder() {
            invocation = new Invocation<>();
        }

        @Nonnull
        public Invocation<T, S> build() {
            return invocation;
        }

        @Nonnull
        public Builder<T, S> withInvocationContext(@Nonnull InvocationContext invocationContext) {
            invocation.invocationContext = invocationContext;

            return this;
        }

        public Builder<T, S> withOperation(@Nonnull T operation) {
            invocation.operation = operation;

            return this;
        }
    }
}
