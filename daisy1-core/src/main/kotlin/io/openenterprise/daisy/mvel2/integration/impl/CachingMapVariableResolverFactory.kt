package io.openenterprise.daisy.mvel2.integration.impl

import com.google.common.collect.Maps
import org.mvel2.integration.VariableResolver
import org.mvel2.integration.impl.CachingMapVariableResolverFactory
import jakarta.annotation.Nonnull

class CachingMapVariableResolverFactory @JvmOverloads constructor(@Nonnull variables: Map<String, Any> = Maps.newHashMap()) :
    CachingMapVariableResolverFactory(variables) {

    init {
        this.variables = variables
    }

    override fun createVariable(name: String, value: Any): VariableResolver {
        val variableResolver = super.createVariable(name, value)
        variables[name] = value

        return variableResolver
    }

    override fun createVariable(name: String, value: Any, type: Class<*>?): VariableResolver {
        val variableResolver = super.createVariable(name, value, type)

        variables[name] = value

        return variableResolver
    }
}
