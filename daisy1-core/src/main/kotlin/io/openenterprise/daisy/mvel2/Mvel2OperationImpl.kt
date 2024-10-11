package io.openenterprise.daisy.mvel2

import com.google.common.collect.Maps
import io.openenterprise.daisy.AbstractOperationImpl
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Parameters
import io.openenterprise.daisy.domain.Parameter
import io.openenterprise.daisy.mvel2.integration.impl.CachingMapVariableResolverFactory
import io.openenterprise.daisy.service.MvelExpressionEvaluationService
import jakarta.inject.Inject
import jakarta.inject.Named
import org.apache.commons.lang3.ClassUtils
import org.mvel2.integration.VariableResolverFactory
import java.util.*
import java.util.function.Consumer
import javax.annotation.Nonnull
import javax.cache.Cache

@Named
open class Mvel2OperationImpl<T> : AbstractOperationImpl<T>(), Mvel2Operation<T> {

    @Inject
    @Named("builtInMvelVariables")
    protected lateinit var builtInMvelVariables: Map<String, Any>

    @Inject
    protected lateinit var mvelExpressionEvaluationService: MvelExpressionEvaluationService

    @Inject
    protected lateinit var variableResolverFactoryCache: Cache<UUID, VariableResolverFactory>

    @Suppress("UNCHECKED_CAST")
    override fun invoke(@Nonnull invocation: Invocation<*, T>): T? {
        val parameters: Parameters = invocation.parameters

        val classImportsAsStringArray: Array<String> = readParameterValue(parameters, Parameter.MVEL_CLASS_IMPORTS)!!
        val classImports = ClassUtils.convertClassNamesToClasses(classImportsAsStringArray.toList()).toSet()

        val packageImportsAsStringArray: Array<String> = readParameterValue(parameters, Parameter.MVEL_PACKAGE_IMPORTS)!!
        val packageImports = packageImportsAsStringArray.toSet()

        val parserContext = mvelExpressionEvaluationService.buildParserContext(classImports,
            packageImports);

        val mvelExpressionsAsStringArray: Array<String> = readParameterValue(parameters, Parameter.MVEL_EXPRESSIONS)!!
        val mvelExpressions = Arrays.stream(mvelExpressionsAsStringArray)
            .map { string -> mvelExpressionEvaluationService.compileExpression(string, parserContext) }
            .toList()

        val variableResolverFactory = variableResolverFactoryCache.get(invocation.invocationContext.id)

        var result: Any? = null
        for (mvelExpression in mvelExpressions) {
            result = mvelExpressionEvaluationService.evaluate(mvelExpression, variableResolverFactory)
        }

        return result as T
    }

    override fun preInvoke(
        @Nonnull invocationContext: InvocationContext, @Nonnull invocation: Invocation<out AbstractOperationImpl<T>, T>,
        @Nonnull parameters: Parameters
    ) {
        super.preInvoke(invocationContext, invocation, parameters)

        val self: Mvel2Operation<T> = this
        val invocationContextId = invocationContext.id
        val callback =
            Consumer { thisVariableResolverFactory: VariableResolverFactory ->
                thisVariableResolverFactory.createVariable("invocationContextId", invocationContextId)
                thisVariableResolverFactory.createVariable("parameters", parameters)
            }

        Optional.of(variableResolverFactoryCache[invocationContextId]).ifPresentOrElse(
            callback
        ) {
            variableResolverFactoryCache.putIfAbsent(
                invocationContextId, self.createVariableResolverFactory(
                    invocationContext, callback
                )
            )
        }
    }

    @Nonnull
    override fun createVariableResolverFactory(
        invocationContext: InvocationContext,
        callback: Consumer<VariableResolverFactory>?
    ): VariableResolverFactory {
        val clone = Maps.newHashMap(builtInMvelVariables)
        clone["invocationContext"] = invocationContext

        val cachingMapVariableResolverFactory = CachingMapVariableResolverFactory(clone)

        if (Objects.nonNull(callback)) {
            callback!!.accept(cachingMapVariableResolverFactory)
        }

        variableResolverFactoryCache.putIfAbsent(invocationContext.id, cachingMapVariableResolverFactory)

        return cachingMapVariableResolverFactory
    }
}
