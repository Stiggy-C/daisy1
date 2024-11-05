package io.openenterprise.daisy.python

import io.openenterprise.daisy.AbstractOperationImpl
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Parameters
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import io.openenterprise.daisy.python.domain.Parameter
import io.openenterprise.graalvm.polyglot.ContextDelegate
import jakarta.inject.Inject
import jakarta.inject.Named
import org.apache.commons.io.file.PathUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.pool2.ObjectPool
import org.mvel2.integration.VariableResolverFactory
import org.springframework.core.io.ClassPathResource
import java.util.Objects
import java.util.function.Consumer
import kotlin.io.path.Path

@Named
class PythonOperationImpl<T>: Mvel2OperationImpl<T>(), PythonOperation<T> {

    @Inject
    @Named("pythonContextDelegatePool")
    lateinit var contextDelegatePool: ObjectPool<ContextDelegate>

    override fun preInvoke(
        invocationContext: InvocationContext,
        invocation: Invocation<out AbstractOperationImpl<T>, T>,
        parameters: Parameters
    ) {
        if (parameters.containsKey(Parameter.PYTHON_SCRIPT_URI) && parameters.containsKey(Parameter.PYTHON_SOURCE)) {
            throw IllegalArgumentException("")
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

        val mvelExpressions = arrayOf("Object pythonEvalResult = python.eval('${pythonSource.trim()}')", "pythonEvalResult")

        parameters[io.openenterprise.daisy.domain.Parameter.MVEL_EXPRESSIONS] = mvelExpressions

        super.preInvoke(invocationContext, invocation, parameters)
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
        return contextDelegatePool.borrowObject().use {
            it.eval("import site;")
            it.eval(source) as T
        }
    }
}