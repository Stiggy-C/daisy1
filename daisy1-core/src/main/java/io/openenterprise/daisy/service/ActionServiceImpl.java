package io.openenterprise.daisy.service;

import io.openenterprise.daisy.Invocation;
import io.openenterprise.daisy.InvocationContext;
import io.openenterprise.daisy.Operation;
import io.openenterprise.daisy.Parameter;
import io.openenterprise.daisy.domain.Action;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Named
public class ActionServiceImpl implements ActionService {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(ActionServiceImpl.class);

    @Inject
    protected Cache<UUID, InvocationContext> invocationContextCache;

    @Nullable
    @Override
    public <T> T invoke(@Nonnull InvocationContext invocationContext, @Nonnull Action<T> action) {
        Invocation<Operation<T>, T> invocation = createInvocation(invocationContext, action.getOperation(),
                action.getParameters());
        invocationContext.setCurrentInvocation(invocation);

        T result = invokeInvocation(invocation);
        invocationContext.archiveCurrentInvocation();

        return result;
    }

    @Override
    public <T> T invoke(@Nonnull UUID invocationContextId, @Nonnull Action<T> action) {
        var invocationContext = getInvocationContext(invocationContextId);

        return invoke(invocationContext, action);
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull InvocationContext invocationContext, @Nonnull List<Action<?>> actions) {
        Object result = null;

        for (Action<?> action : actions) {
            result = invoke(invocationContext, action);
        }

        return result;
    }

    @Override
    public Object invoke(@Nonnull UUID invocationContextId, @Nonnull List<Action<?>> actions) {
        var invocationContext = getInvocationContext(invocationContextId);

        return invoke(invocationContext, actions);
    }


    @Nonnull
    protected <T extends Operation<S>, S> Invocation<T , S> createInvocation(
            @Nonnull InvocationContext invocationContext, @Nonnull T operation, @Nonnull Map<Parameter, Object> parameters) {
        var invocation = new Invocation<T, S>();

        invocation.setInvocationContext(invocationContext);
        invocation.setOperation(operation);
        invocation.setParameters(parameters);

        return invocation;
    }

    @Nonnull
    protected InvocationContext getInvocationContext(@Nonnull UUID invocationContextId) {
        if (!invocationContextCache.containsKey(invocationContextId)) {
            throw new IllegalStateException("No InvocationContext");
        }

        return invocationContextCache.get(invocationContextId);
    }

    @Nullable
    protected <T extends Operation<S>, S> S invokeInvocation(@Nonnull Invocation<T, S> invocation) {
        invocation.setInvocationDateTime(OffsetDateTime.now());

        S result;
        try {
            result = invocation.getOperation().invoke(invocation);
            invocation.setResult(result);
            invocation.setCompletionDateTime(OffsetDateTime.now());
        } catch (Throwable t) {
            String logMessage = """
                    Error occurred when invoking Operation, {}, with Parameters, {}. Error: {}
                    """;

            LOGGER.error(logMessage, invocation.getOperation(), invocation.getParameters(), t);
            
            invocation.setThrowable(t);

            throw t;
        }

        return result;
    }
}
