package io.openenterprise.daisy;

import com.github.f4b6a3.uuid.UuidCreator;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Invocation<T extends Operation<S>, S> implements Serializable {

    public Invocation() {
        id = UuidCreator.getTimeOrderedWithRandom();
        createdInstant = Instant.now();
    }

    protected UUID id;

    protected Instant completionInstant;

    protected Instant createdInstant;

    protected InvocationContext invocationContext;

    protected Instant invocationInstant;

    protected T operation;

    protected Parameters parameters;

    protected S result;

    protected Throwable throwable;

    public UUID getId() {
        return id;
    }

    public InvocationContext getInvocationContext() {
        return invocationContext;
    }

    public T getOperation() {
        return this.operation;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public S getResult() {
        return this.result;
    }

    public void setCompletionInstant(Instant completionInstant) {
        this.completionInstant = completionInstant;
    }

    public void setCreatedInstant(Instant createdInstant) {
        this.createdInstant = createdInstant;
    }

    public void setInvocationContext(InvocationContext invocationContext) {
        this.invocationContext = invocationContext;
    }

    public void setInvocationInstant(Instant invocationInstant) {
        this.invocationInstant = invocationInstant;
    }

    public void setOperation(T operation) {
        this.operation = operation;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public void setResult(S result) {
        this.result = result;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

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
