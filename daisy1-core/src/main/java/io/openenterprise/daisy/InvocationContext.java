package io.openenterprise.daisy;

import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.Nonnull;
import java.util.*;

@Getter
public class InvocationContext {

    @Setter
    protected UUID id;

    protected Invocation<? extends Operation<?>, ?> currentInvocation;

    protected SortedSet<Invocation<? extends Operation<?>, ?>> previousInvocations;

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

        currentInvocation = null;
    }

    public SortedSet<Invocation<? extends Operation<?>, ?>> getPreviousInvocation() {
        return previousInvocations;
    }

    public <T extends Operation<S>, S> void setCurrentInvocation(@Nonnull Invocation<T, S> currentInvocation) {
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
