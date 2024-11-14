package io.openenterprise.daisy.mvel2

import com.google.common.collect.Maps
import io.openenterprise.daisy.AbstractOperationImpl
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Operation
import io.openenterprise.daisy.Parameters
import io.openenterprise.daisy.mvel2.domain.Parameter
import io.openenterprise.daisy.mvel2.integration.impl.CachingMapVariableResolverFactory
import io.openenterprise.daisy.service.Mvel2EvaluationService
import jakarta.inject.Inject
import jakarta.inject.Named
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.ClassUtils
import org.mvel2.MVEL
import org.mvel2.integration.VariableResolverFactory
import java.util.*
import java.util.function.Consumer
import javax.annotation.Nonnull
import javax.annotation.Nullable
import javax.cache.Cache

@Named
open class Mvel2OperationImpl<T> : AbstractOperationImpl<T>(), Mvel2Operation<T> {

    @Inject
    @Named("builtInMvelVariables")
    protected lateinit var builtInMvelVariables: Map<String, Any>

    @Inject
    protected lateinit var mvel2EvaluationService: Mvel2EvaluationService

    @Inject
    protected lateinit var variableResolverFactoryCache: Cache<UUID, VariableResolverFactory>

    override fun preInvoke(
        @Nonnull invocationContext: InvocationContext,
        @Nonnull invocation: Invocation<out Operation<T>, T>,
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

    @Suppress("UNCHECKED_CAST")
    override fun invoke(@Nonnull invocation: Invocation<*, T>): T? {
        val invocationContextId = invocation.invocationContext.id
        val parameters: Parameters = invocation.parameters
        val classesToImports = getClassesToImport(parameters)
        val packagesToImports = getPackagesToImport(parameters)
        val mvelExpressions: Array<String> = readParameterValue(parameters, Parameter.MVEL_EXPRESSIONS)!!
        var result: Any? = eval(invocationContextId, classesToImports, packagesToImports, mvelExpressions)

        return result as T
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

    protected fun eval(
        invocationContextId: UUID, classesToImport: Set<Class<*>>, packagesToImport: Set<String>,
        mvelExpressionsAsStringArray: Array<String>
    ): Any? {
        val parserContext = mvel2EvaluationService.buildParserContext(classesToImport, packagesToImport)
        val variableResolverFactory = variableResolverFactoryCache.get(invocationContextId)

        var result: Any? = null
        for (mvelExpressionString in mvelExpressionsAsStringArray) {
            // result = MVEL.eval(mvelExpressionString, parserContext, variableResolverFactory)
            val executableStatement = mvel2EvaluationService.compileExpression(mvelExpressionString, parserContext)
            result = mvel2EvaluationService.evaluate(executableStatement, variableResolverFactory)
        }

        return result
    }

    protected fun getClassesToImport(parameters: Parameters): Set<Class<*>> {
        val classImportsAsStringArray: Array<String>? = readParameterValue(parameters, Parameter.MVEL_CLASS_IMPORTS)
        val classImports = if (ArrayUtils.isEmpty(classImportsAsStringArray)) {
            Collections.emptySet<Class<*>>()
        } else {
            ClassUtils.convertClassNamesToClasses(classImportsAsStringArray!!.toList()).toSet()
        }
        return classImports
    }

    protected fun getPackagesToImport(parameters: Parameters): Set<String> {
        val packageImportsAsStringArray: Array<String>? = readParameterValue(parameters, Parameter.MVEL_PACKAGE_IMPORTS)
        val packageImports = if (ArrayUtils.isEmpty(packageImportsAsStringArray)) {
            Collections.emptySet<String>()
        } else {
            packageImportsAsStringArray!!.toSet()
        }
        return packageImports
    }
}
