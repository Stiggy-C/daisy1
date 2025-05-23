package io.openenterprise.daisy.spark.sql

import io.openenterprise.commons.util.UUIDUtils
import io.openenterprise.daisy.AbstractOperationImpl
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Parameter
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import io.openenterprise.daisy.mvel2.service.Mvel2EvaluationService
import io.openenterprise.daisy.mvel2.service.Mvel2EvaluationServiceImpl
import io.openenterprise.test.junit.jupiter.SparkExtension
import org.apache.commons.lang3.reflect.MethodUtils
import org.apache.spark.sql.Dataset
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.system.exitProcess

@ExtendWith(value = [SparkExtension::class, SpringExtension::class])
@ComponentScan(basePackages = ["io.openenterprise.daisy.springframework.boot.autoconfigure",
    "io.openenterprise.daisy.springframework.boot.autoconfigure.spark"])
@Import(SparkOperationImplTest.Configuration::class)
open class SparkOperationImplTest {

    companion object {

        @AfterAll
        @JvmStatic
        protected fun afterAll() {
            exitProcess(0)
        }
    }

    @Autowired
    protected lateinit var sparkOperation: SparkOperationImpl<Any>

    @Test
    open fun `Test invoke`() {
        val invocationContext = createTestInvocationContext()
        @Suppress("UNCHECKED_CAST")
        val invocation = invocationContext.currentInvocation as Invocation<Mvel2OperationImpl<Any>, Any>
        val parameters = mutableMapOf<Parameter, Any>()
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_EXPRESSIONS] = arrayOf("sparkSession.read().json(sparkSession.createDataset(" +
                "Lists.newArrayList('{\"id\": 0}', '{\"id\": 1}'), Encoders.STRING()))")
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf("org.apache.spark.sql")

        invocationContext.currentInvocation.parameters = parameters

        sparkOperation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(sparkOperation, true,"preInvoke", invocationContext, invocation,
            parameters)

        @Suppress("UNCHECKED_CAST")
        val result: Any = sparkOperation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)!!

        Assertions.assertNotNull(result)
        Assertions.assertTrue(result is Dataset<*>)
        Assertions.assertEquals(2, (result as Dataset<*>).count())
    }

    protected open fun createTestInvocationContext(): InvocationContext {
        val invocationContext = InvocationContext(UUIDUtils.randomUUIDv7())
        val invocation: Invocation<in AbstractOperationImpl<Any>, Any> = Invocation()
        invocation.operation = sparkOperation
        invocation.invocationContext = invocationContext

        return invocationContext.apply { setCurrentInvocation(invocation) }
    }

    @TestConfiguration
    class Configuration {


        @Bean
        fun mvelExpressionEvaluationService(): Mvel2EvaluationService {
            return Mvel2EvaluationServiceImpl()
        }

        @Bean
        fun sparkOperation(): SparkOperation<Any> {
            return SparkOperationImpl()
        }
    }
}