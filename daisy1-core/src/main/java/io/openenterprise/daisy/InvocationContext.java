package io.openenterprise.daisy;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.*;

@Getter
public class InvocationContext {

    @Setter
    protected UUID id;

    protected Invocation<?, ?> currentInvocation;

    protected SortedSet<Invocation<?, ?>> previousInvocations;

    protected InvocationContext() {
        previousInvocations = new TreeSet<>(Comparator.comparing(Invocation::getId));
    }

    public InvocationContext(@Nonnull UUID id) {
        this();
        this.id = id;
    }

    public void archiveCurrentInvocation() {
        assert Objects.nonNull(currentInvocation);

        previousInvocations.add(currentInvocation);
    }

    public UUID getId() {
        return id;
    }

    public Invocation<?, ?> getCurrentInvocation() {
        return currentInvocation;
    }

    public SortedSet<Invocation<?, ?>> getPreviousInvocation() {
        return previousInvocations;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public <T extends AbstractOperationImpl<S>, S> void setCurrentInvocation(@Nonnull Invocation<T, S> currentInvocation) {
        this.currentInvocation = currentInvocation;
    }

    public static class Builder {

        private final InvocationContext invocationContext;

        public Builder() {
            invocationContext = new InvocationContext();
        }

        @Nonnull
        public InvocationContext build() {
            return invocationContext;
        }

        @Nonnull
        public Builder withId(@Nonnull UUID id) {
            invocationContext.id = id;

            return this;
        }
    }
}
