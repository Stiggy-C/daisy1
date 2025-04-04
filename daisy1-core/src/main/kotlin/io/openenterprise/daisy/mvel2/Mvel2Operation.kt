package io.openenterprise.daisy.mvel2

import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Operation
import org.mvel2.integration.VariableResolverFactory
import java.util.function.Consumer
import jakarta.annotation.Nonnull

interface Mvel2Operation<T> : Operation<T> {

    @Nonnull
    fun createVariableResolverFactory(
        @Nonnull invocationContext: InvocationContext,
        callback: Consumer<VariableResolverFactory>?
    ): VariableResolverFactory
}
