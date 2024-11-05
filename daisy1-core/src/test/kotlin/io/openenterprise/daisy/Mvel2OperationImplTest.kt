package io.openenterprise.daisy

import com.github.f4b6a3.uuid.UuidCreator
import io.openenterprise.daisy.domain.Parameter
import io.openenterprise.daisy.mvel2.Mvel2Operation
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import io.openenterprise.daisy.service.Mvel2EvaluationService
import io.openenterprise.daisy.service.Mvel2EvaluationServiceImpl
import org.apache.commons.lang3.reflect.MethodUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.mvel2.integration.VariableResolverFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import javax.cache.Cache

@ExtendWith(SpringExtension::class)
@TestMethodOrder(OrderAnnotation::class)
@ComponentScan(basePackages = ["io.openenterprise.daisy.springframework.boot.autoconfigure"])
@Import(Mvel2OperationImplTest.Configuration::class)
class Mvel2OperationImplTest {

    @Autowired
    lateinit var mvel2Operation: Mvel2OperationImpl<Any>

    @Autowired
    lateinit var variableResolverFactoryCache: Cache<UUID, VariableResolverFactory>

    @Order(2)
    @Test
    fun `Test invoke`() {
        val invocationContext = createTestInvocationContext()
        val parameters = Parameters()
        parameters[Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[Parameter.MVEL_EXPRESSIONS] = arrayOf("variable = 0;variable")
        parameters[Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf<String>()

        invocationContext.currentInvocation.parameters = parameters

        mvel2Operation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(mvel2Operation, true,"preInvoke", invocationContext,
            invocationContext.currentInvocation, parameters)

        @Suppress("UNCHECKED_CAST")
        val result: Any = mvel2Operation.invoke(invocationContext.currentInvocation as Invocation<*, Any>)!!

        assertNotNull(result)
    }

    @Order(1)
    @Test
    fun `Test preInvoke`() {
        val invocationContext = createTestInvocationContext()
        @Suppress("UNCHECKED_CAST")
        val invocation = invocationContext.currentInvocation as Invocation<Mvel2OperationImpl<Any>, Any>
        val parameters = Parameters()
        parameters[Parameter.MVEL_CLASS_IMPORTS] = arrayOf<String>()
        parameters[Parameter.MVEL_EXPRESSIONS] = arrayOf("variable = 0;variable")
        parameters[Parameter.MVEL_PACKAGE_IMPORTS] = arrayOf<String>()

        mvel2Operation.createVariableResolverFactory(invocationContext, null)
        MethodUtils.invokeMethod(mvel2Operation, true,"preInvoke", invocationContext, invocation,
            parameters)

        assertTrue(variableResolverFactoryCache.containsKey(invocationContext.id))

        val variableResolverFactory = variableResolverFactoryCache[invocationContext.id]

        assertNotNull(variableResolverFactory)

        val variableResolverFactoryKnownVariables = variableResolverFactory.knownVariables

        assertFalse(variableResolverFactoryKnownVariables.isEmpty())
        assertTrue(variableResolverFactoryKnownVariables.contains("invocationContextId"))
        assertTrue(variableResolverFactoryKnownVariables.contains("parameters"))
    }

    @Order(0)
    @Test
    fun `Test createVariableResolverFactory`() {
        val invocationContext = createTestInvocationContext()

        mvel2Operation.createVariableResolverFactory(invocationContext, null)

        assertTrue(variableResolverFactoryCache.containsKey(invocationContext.id))

        val variableResolverFactory = variableResolverFactoryCache[invocationContext.id]

        assertNotNull(variableResolverFactory)

        val variableResolverFactoryKnownVariables = variableResolverFactory.knownVariables

        assertFalse(variableResolverFactoryKnownVariables.isEmpty())
        assertTrue(variableResolverFactoryKnownVariables.contains("invocationContext"))
    }

    private fun createTestInvocationContext(): InvocationContext {
        val invocationContext = InvocationContext(UuidCreator.getTimeOrderedWithRandom())
        val invocation: Invocation<in AbstractOperationImpl<Any>, Any> = Invocation()
        invocation.operation = mvel2Operation
        invocation.invocationContext = invocationContext

        return invocationContext.apply { setCurrentInvocation(invocation) }
    }

    @TestConfiguration
    class Configuration {

        @Bean
        fun mvel2Operation(): Mvel2Operation<Any> {
            return Mvel2OperationImpl()
        }

        @Bean
        fun mvelExpressionEvaluationService(): Mvel2EvaluationService {
            return Mvel2EvaluationServiceImpl()
        }
    }
}