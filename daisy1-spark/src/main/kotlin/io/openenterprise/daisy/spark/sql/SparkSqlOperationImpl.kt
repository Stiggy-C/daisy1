package io.openenterprise.daisy.spark.sql

import io.openenterprise.daisy.Invocation
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.Operation
import io.openenterprise.daisy.Parameter
import jakarta.inject.Named

@Named
class SparkSqlOperationImpl<T> : SparkOperationImpl<T>(), SparkSqlOperation<T> {

    override fun preInvoke(
        invocationContext: InvocationContext,
        invocation: Invocation<out Operation<T>, T>,
        parameters: Map<Parameter, Any>
    ) {
        val sparkSqlStatements: Array<String> = readParameterValue(parameters, io.openenterprise.daisy.spark.Parameter.SQL_STATEMENTS)!!
        val mvelExpressions = sparkSqlStatements.toList().stream().map { "sparkSession.sql(\"$it\")" }
            .toArray { arrayOfNulls<String>(it) }

        (parameters as MutableMap)[io.openenterprise.daisy.mvel2.Parameter.MVEL_EXPRESSIONS] = mvelExpressions

        super.preInvoke(invocationContext, invocation, parameters)
    }
}