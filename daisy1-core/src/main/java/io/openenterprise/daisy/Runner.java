package io.openenterprise.daisy;

import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface Runner {

    void run(@Nonnull InvocationContext invocationContext,
             @Nonnull List<Pair<Operation<?>, Parameters>> operationAndParameterPairs);

    void run(@Nonnull UUID invocationContextId, @Nonnull List<Pair<Operation<?>, Parameters>> operationAndParameterPairs);
}
