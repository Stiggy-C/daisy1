package io.openenterprise.daisy.spark

import io.openenterprise.daisy.AbstractOperationImpl
import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Parameters
import io.openenterprise.daisy.spark.domain.Parameter
import jakarta.inject.Named

@Named
class SparkSqlOperationImpl<T> : SparkOperationImpl<T>(), SparkSqlOperation<T> {

    override fun preInvoke(
        invocationContext: InvocationContext,
        invocation: Invocation<out AbstractOperationImpl<T>, T>,
        parameters: Parameters
    ) {
        val sparkSqlStatements: Array<String> = readParameterValue(parameters, Parameter.SQL_STATEMENTS)!!
        val mvelExpressions = sparkSqlStatements.toList().stream().map { "sparkSession.sql(\"$it\")" }
            .toArray { arrayOfNulls<String>(it) }

        parameters[io.openenterprise.daisy.domain.Parameter.MVEL_EXPRESSIONS] = mvelExpressions

        super.preInvoke(invocationContext, invocation, parameters)
    }
}