package io.openenterprise.daisy.python

import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Parameters
import io.openenterprise.daisy.graalvm.PythonOperation
import io.openenterprise.daisy.graalvm.PythonOperationImpl
import io.openenterprise.daisy.python.domain.Parameter
import io.openenterprise.daisy.service.Mvel2EvaluationService
import io.openenterprise.daisy.service.Mvel2EvaluationServiceImpl
import io.openenterprise.test.junit.jupiter.SparkExtension
import org.apache.commons.lang3.reflect.MethodUtils
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.ThrowingSupplier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID
import kotlin.system.exitProcess

@ExtendWith(value = [SpringExtension::class])
@TestMethodOrder(OrderAnnotation::class)
@ComponentScan(basePackages = ["io.openenterprise.daisy.springframework.boot.autoconfigure",
    "io.openenterprise.daisy.springframework.boot.autoconfigure.graalvm"])
@Import(PythonOperationImplTest.Configuration::class)
@TestPropertySource(properties = ["python.executable=./graalpy/.venv/bin/python"])
class PythonOperationImplTest {

    companion object {

        @AfterAll
        @JvmStatic
        protected fun afterAll() {
            exitProcess(0)
        }
    }

    @Autowired
    lateinit var pythonOperation: PythonOperation<Any>

    @Test
    fun `Test invoke`() {
        val invocationContext: InvocationContext = InvocationContext.Builder().withId(UUID.randomUUID()).build()

        val invocation: Invocation<PythonOperationImpl<Any>, Any> = Invocation.Builder<PythonOperationImpl<Any>, Any>()
            .withInvocationContext(invocationContext).withOperation(pythonOperation as PythonOperationImpl<Any>).build()
        val parameters = Parameters()

        parameters[Parameter.PYTHON_SCRIPT_URI] = "classpath:python_operation_impl_test.py"

        invocation.parameters = parameters
        invocationContext.setCurrentInvocation(invocation)

        val variableResolverFactory = pythonOperation.createVariableResolverFactory(invocationContext, null)

        variableResolverFactory.createVariable("sparkConnectHost", "sc://localhost:15002")

        parameters[Parameter.PYTHON_PRE_EVAL_SOURCE] = arrayOf("'x = ' + 2 + '; y = ' + 3 + ';'")
        MethodUtils.invokeMethod(
            pythonOperation, true, "preInvoke", invocationContext, invocation,
            parameters
        )
        @Suppress("UNCHECKED_CAST")
        val throwingSupplier: ThrowingSupplier<Object> = ThrowingSupplier {
            pythonOperation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)!! as Object
        }
        val result: Object = assertDoesNotThrow(throwingSupplier)

        assertNotNull(result)
    }

    @TestConfiguration
    class Configuration {

        @Bean
        fun mvelExpressionEvaluationService(): Mvel2EvaluationService {
            return Mvel2EvaluationServiceImpl()
        }

        @Bean
        fun pythonOperation(): PythonOperation<Any> {
            return PythonOperationImpl()
        }
    }
}