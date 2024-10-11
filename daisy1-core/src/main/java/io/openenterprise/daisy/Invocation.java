package io.openenterprise.daisy;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Invocation<T extends AbstractOperationImpl<S>, S> implements Serializable {

    public Invocation() {
        id = UuidCreator.getTimeOrderedWithRandom();
        createdInstant = Instant.now();
    }

    protected UUID id;

    @Setter
    protected Instant completionInstant;

    @Setter
    protected Instant createdInstant;

    @Setter
    protected InvocationContext invocationContext;

    @Setter
    protected Instant invocationInstant;

    @Setter
    protected T operation;

    @Setter
    protected Parameters parameters;

    @Setter
    protected S result;

    @Setter
    protected Throwable throwable;
}
