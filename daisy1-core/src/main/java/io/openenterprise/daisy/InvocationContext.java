package io.openenterprise.daisy;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

@Getter
@Setter
public class InvocationContext {

    protected UUID id;

    protected Invocation<?, ?> currentInvocation;

    protected SortedSet<Invocation<?, ?>> previousInvocation;

    protected InvocationContext() {
        previousInvocation = new TreeSet<>(Comparator.comparing(Invocation::getId));
    }

    public InvocationContext(@Nonnull UUID id) {
        this();
        this.id = id;
    }

    public  <T extends AbstractOperationImpl<S>, S> void setCurrentInvocation(@Nonnull Invocation<T, S> currentInvocation) {
        this.currentInvocation = currentInvocation;
    }
}
