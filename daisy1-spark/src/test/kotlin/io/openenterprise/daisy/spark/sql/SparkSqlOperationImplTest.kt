package io.openenterprise.daisy.spark.sql

import io.openenterprise.commons.util.UUIDUtils
import io.openenterprise.daisy.AbstractOperationImpl
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Parameter
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import io.openenterprise.daisy.spark.sql.SparkOperationImplTest
import org.apache.commons.lang3.reflect.MethodUtils
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.junit.jupiter.api.Assertions
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
        var parameters = mutableMapOf<Parameter, Any>()
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_EXPRESSIONS] = arrayOf("dataset = sparkSession.read().json(sparkSession.createDataset(" +
                "Lists.newArrayList('{\"id\": 0}', '{\"id\": 1}'), Encoders.STRING()))", "dataset.createOrReplaceTempView(" +
                "\"${SparkSqlOperationImplTest::class.simpleName}\")")
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf("org.apache.spark.sql")

        invocationContext.currentInvocation.parameters = parameters

        sparkOperation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(sparkOperation, true,"preInvoke", invocationContext, invocation,
            parameters)

        @Suppress("UNCHECKED_CAST")
        var result: Any? = sparkOperation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)

        Assertions.assertNull(result)
        Assertions.assertNotNull(sparkSession.sql("select * from ${SparkSqlOperationImplTest::class.simpleName}"))

        invocationContext = this.createTestInvocationContext()
        parameters = mutableMapOf<Parameter, Any>()
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[io.openenterprise.daisy.spark.Parameter.SQL_STATEMENTS] = arrayOf(
            "select * from sparkSqlOperationImplTest")
        parameters[io.openenterprise.daisy.mvel2.Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf("org.apache.spark.sql")

        invocationContext.currentInvocation.parameters = parameters

        sparkSqlOperation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(sparkSqlOperation, true,"preInvoke", invocationContext,
            invocation, parameters)

        @Suppress("UNCHECKED_CAST")
        result = sparkSqlOperation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)!!

        Assertions.assertNotNull(result)
        Assertions.assertTrue(result is Dataset<*>)
        Assertions.assertEquals(2, (result as Dataset<*>).count())
    }

    override fun createTestInvocationContext(): InvocationContext {
        val invocationContext = InvocationContext(UUIDUtils.randomUUIDv7())
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