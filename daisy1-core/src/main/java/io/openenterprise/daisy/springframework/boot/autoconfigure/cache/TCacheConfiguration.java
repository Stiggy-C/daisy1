package io.openenterprise.daisy.springframework.boot.autoconfigure.cache;

import com.trivago.triava.tcache.EvictionPolicy;
import com.trivago.triava.tcache.TCacheFactory;
import com.trivago.triava.tcache.core.Builder;
import io.openenterprise.daisy.InvocationContext;
import io.openenterprise.daisy.springframework.boot.autoconfigure.ApplicationConfiguration;
import org.mvel2.integration.VariableResolverFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@AutoConfigureAfter(ApplicationConfiguration.class)
@ConditionalOnClass(TCacheFactory.class)
public class TCacheConfiguration {

    @Bean
    protected javax.cache.configuration.Configuration<UUID, InvocationContext> invocationContextCacheConfiguration() {
        return new Builder<UUID, InvocationContext>().setEvictionPolicy(EvictionPolicy.LFU);

    }

    @Bean
    protected javax.cache.configuration.Configuration<UUID, VariableResolverFactory>
            variableResolverFactoryCacheConfiguration() {
        return new Builder<UUID, VariableResolverFactory>().setEvictionPolicy(EvictionPolicy.LFU);

    }
}
