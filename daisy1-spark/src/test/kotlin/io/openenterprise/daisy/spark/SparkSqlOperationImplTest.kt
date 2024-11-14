package io.openenterprise.daisy.spark

import com.github.f4b6a3.uuid.UuidCreator
import io.openenterprise.daisy.*
import io.openenterprise.daisy.mvel2.domain.Parameter
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import org.apache.commons.lang3.reflect.MethodUtils
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@Import(value = [SparkOperationImplTest.Configuration::class, SparkSqlOperationImplTest.Configuration::class])
class SparkSqlOperationImplTest : SparkOperationImplTest() {

    @Autowired
    lateinit var sparkSession: SparkSession

    @Autowired
    lateinit var sparkSqlOperation: SparkSqlOperationImpl<Any>

    @Test
    override fun `Test invoke`() {
        var invocationContext = super.createTestInvocationContext()
        @Suppress("UNCHECKED_CAST")
        val invocation = invocationContext.currentInvocation as Invocation<Mvel2OperationImpl<Any>, Any>
        var parameters = Parameters()
        parameters[Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[Parameter.MVEL_EXPRESSIONS] = arrayOf("dataset = sparkSession.read().json(sparkSession.createDataset(" +
                "Lists.newArrayList('{\"id\": 0}', '{\"id\": 1}'), Encoders.STRING()))", "dataset.createOrReplaceTempView(" +
                "\"${SparkSqlOperationImplTest::class.simpleName}\")")
        parameters[Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf("org.apache.spark.sql")

        invocationContext.currentInvocation.parameters = parameters

        sparkOperation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(sparkOperation, true,"preInvoke", invocationContext, invocation,
            parameters)

        @Suppress("UNCHECKED_CAST")
        var result: Any? = sparkOperation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)

        assertNull(result)
        assertNotNull(sparkSession.sql("select * from ${SparkSqlOperationImplTest::class.simpleName}"))

        invocationContext = this.createTestInvocationContext()
        parameters = Parameters()
        parameters[Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[io.openenterprise.daisy.spark.domain.Parameter.SQL_STATEMENTS] = arrayOf(
            "select * from sparkSqlOperationImplTest")
        parameters[Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf("org.apache.spark.sql")

        invocationContext.currentInvocation.parameters = parameters

        sparkSqlOperation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(sparkSqlOperation, true,"preInvoke", invocationContext,
            invocation, parameters)

        @Suppress("UNCHECKED_CAST")
        result = sparkSqlOperation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)!!

        assertNotNull(result)
        assertTrue(result is Dataset<*>)
        assertEquals(2, (result as Dataset<*>).count())
    }

    override fun createTestInvocationContext(): InvocationContext {
        val invocationContext = InvocationContext(UuidCreator.getTimeOrderedWithRandom())
        val invocation: Invocation<in AbstractOperationImpl<Any>, Any> = Invocation()
        invocation.operation = sparkSqlOperation
        invocation.invocationContext = invocationContext

        return invocationContext.apply { setCurrentInvocation(invocation) }
    }

    @TestConfiguration
    class Configuration {

        @Bean
        fun sparkSqlOperation(): SparkSqlOperation<Any> {
            return SparkSqlOperationImpl()
        }
    }
}