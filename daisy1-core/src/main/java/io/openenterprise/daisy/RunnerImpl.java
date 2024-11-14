package io.openenterprise.daisy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.cache.Cache;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Named
public class RunnerImpl implements Runner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RunnerImpl.class);

    @Inject
    protected Cache<UUID, InvocationContext> invocationContextCache;

    @Override
    public void run(@Nonnull InvocationContext invocationContext,
                    @Nonnull List<Pair<Operation<?>, Parameters>> operationAndParameterPairs) {
        var invocations = operationAndParameterPairs.stream()
                .map(pair -> createInvocation(invocationContext, pair.getKey(), pair.getValue()))
                .toList();
        var runner = this;

        invocations.forEach(invocation -> {
            invocationContext.setCurrentInvocation(invocation);
            runner.runOperation(invocation);
            invocationContext.archiveCurrentInvocation();
        });
    }

    @Override
    public void run(@Nonnull UUID invocationContextId,
                    @Nonnull List<Pair<Operation<?>, Parameters>> operationAndParameterPairs) {
        var invocationContext = Optional.ofNullable(invocationContextCache.get(invocationContextId)).orElseGet(() -> {
            var thisInvocationContext = new InvocationContext.Builder().withId(invocationContextId).build();

            try {
                return thisInvocationContext;
            } finally {
                invocationContextCache.putIfAbsent(invocationContextId, thisInvocationContext);
            }
        });

        run(invocationContext, operationAndParameterPairs);
    }

    protected <T extends Operation<S>, S> Invocation<T , S> createInvocation(
            @Nonnull InvocationContext invocationContext, @Nonnull T operation, @Nonnull Parameters parameters) {
        var invocation = new Invocation<T, S>();

        invocation.invocationContext = invocationContext;
        invocation.operation = operation;
        invocation.parameters = parameters;

        return invocation;
    }

    protected <T extends Operation<S>, S> void runOperation(@Nonnull Invocation<T, S> invocation) {
        try {
            invocation.setInvocationInstant(Instant.now());
            invocation.result = invocation.operation.invoke(invocation);
            invocation.setCompletionInstant(Instant.now());
        } catch (Throwable t) {
            String logMessage = """
                    Error occurred when invoking Operation, {}, with Parameters, {}. Error: {}
                    """;

            LOGGER.error(logMessage, invocation.operation, invocation.parameters, t);
        }
    }
}
