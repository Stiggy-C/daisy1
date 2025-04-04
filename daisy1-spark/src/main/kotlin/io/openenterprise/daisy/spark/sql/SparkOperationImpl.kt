package io.openenterprise.daisy.spark.sql

import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl
import jakarta.inject.Inject
import jakarta.inject.Named
import org.apache.spark.sql.SparkSession
import org.mvel2.integration.VariableResolverFactory
import java.util.*
import java.util.function.Consumer

@Named
open class SparkOperationImpl<T> : Mvel2OperationImpl<T>(), SparkOperation<T> {

    @Inject
    protected lateinit var sparkSession: SparkSession

    override fun createVariableResolverFactory(
        invocationContext: InvocationContext,
        callback: Consumer<VariableResolverFactory>?
    ): VariableResolverFactory {
        val thisCallback =
            Consumer { thisVariableResolverFactory: VariableResolverFactory ->
                thisVariableResolverFactory.createVariable("sparkSession", sparkSession)
            }

        val actualCallback = if (Objects.isNull(callback)) thisCallback else callback!!.andThen(thisCallback)

        return super.createVariableResolverFactory(invocationContext, actualCallback)
    }

}