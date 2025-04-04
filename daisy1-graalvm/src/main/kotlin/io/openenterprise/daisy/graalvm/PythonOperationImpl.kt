package io.openenterprise.daisy.graalvm

import com.google.common.base.CaseFormat
import com.google.common.collect.Lists
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Operation
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import io.openenterprise.daisy.python.Parameter
import io.openenterprise.graalvm.polyglot.ContextDelegate
import jakarta.inject.Inject
import jakarta.inject.Named
import org.apache.commons.io.file.PathUtils
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.pool2.ObjectPool
import org.mvel2.MVEL
import org.mvel2.integration.VariableResolverFactory
import org.springframework.core.io.ClassPathResource
import java.util.Objects
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.io.path.Path

@Named
class PythonOperationImpl<T> : Mvel2OperationImpl<T>(), PythonOperation<T> {

    companion object {

        const val DEFAULT_PYTHON_PRE_EVAL_SOURCE = "'import site;'"

    }

    @Inject
    @Named("pythonContextDelegatePool")
    lateinit var contextDelegatePool: ObjectPool<ContextDelegate>

    override fun preInvoke(
        invocationContext: InvocationContext,
        invocation: Invocation<out Operation<T>, T>,
        parameters: Map<io.openenterprise.daisy.Parameter, Any>
    ) {
        if (parameters.containsKey(Parameter.PYTHON_SCRIPT_URI) && parameters.containsKey(Parameter.PYTHON_SOURCE)) {
            throw IllegalArgumentException("")
        }

        super.preInvoke(invocationContext, invocation, parameters)

        val invocationContextId = invocationContext.id

        val preEvalPythonSource = if (parameters.containsKey(Parameter.PYTHON_PRE_EVAL_SOURCE)) {
            var preEvalMvelExpressions: Array<String> =
                readParameterValue(parameters, Parameter.PYTHON_PRE_EVAL_SOURCE)!!

            if (ArrayUtils.isEmpty(preEvalMvelExpressions)) {
                DEFAULT_PYTHON_PRE_EVAL_SOURCE
            } else {
                val classesToImports = getClassesToImport(parameters)
                val packagesToImports = getPackagesToImport(parameters)
                val parserContext = mvel2EvaluationService.buildParserContext(classesToImports, packagesToImports)
                val variableResolverFactory = variableResolverFactoryCache.get(invocationContextId)

                Lists.asList<String>(DEFAULT_PYTHON_PRE_EVAL_SOURCE, preEvalMvelExpressions).stream()
                    .map { it -> MVEL.eval(it, parserContext, variableResolverFactory, String::class.java) }
                    .map { it -> if (StringUtils.contains(it, ";")) it else "$it;"}
                    .collect(Collectors.joining("\n"))
            }
        } else {
            DEFAULT_PYTHON_PRE_EVAL_SOURCE
        }

        val pythonSource: String = if (parameters.containsKey(Parameter.PYTHON_SCRIPT_URI)) {
            var uriAsString: String = readParameterValue(parameters, Parameter.PYTHON_SCRIPT_URI)!!

            if (StringUtils.startsWith(uriAsString, "classpath:")) {
                ClassPathResource(StringUtils.removeStart(uriAsString, "classpath:")).getContentAsString(Charsets.UTF_8)
            } else {
                PathUtils.readString(Path(uriAsString), Charsets.UTF_8)
            }
        } else {
            readParameterValue(parameters, Parameter.PYTHON_SOURCE)!!
        }

        val pythonOpResultVariableName =
            "pythonOpResult${CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, invocation.id.toString())}"
        val mvelExpressions = arrayOf(
            "$pythonOpResultVariableName = python.eval('$preEvalPythonSource\n${pythonSource}')",
            pythonOpResultVariableName
        )

        (parameters as MutableMap)[io.openenterprise.daisy.mvel2.Parameter.MVEL_EXPRESSIONS] = mvelExpressions
    }

    override fun createVariableResolverFactory(
        invocationContext: InvocationContext,
        callback: Consumer<VariableResolverFactory>?
    ): VariableResolverFactory {
        val thisCallback =
            Consumer { thisVariableResolverFactory: VariableResolverFactory ->
                thisVariableResolverFactory.createVariable("python", this)
            }

        val actualCallback = if (Objects.isNull(callback)) thisCallback else callback!!.andThen(thisCallback)

        return super.createVariableResolverFactory(invocationContext, actualCallback)
    }

    @Suppress("UNCHECKED_CAST")
    override fun eval(source: String): T? {
        val contextDelegate: ContextDelegate = contextDelegatePool.borrowObject()

        try {
            return contextDelegate.use { it.eval(source) as T }
        } finally {
            contextDelegatePool.returnObject(contextDelegate)
        }
    }
}