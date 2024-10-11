package io.openenterprise.daisy.springframework.boot.autoconfigure.cache;

import io.openenterprise.daisy.InvocationContext;
import io.openenterprise.daisy.springframework.boot.autoconfigure.ApplicationConfiguration;
import org.mvel2.integration.VariableResolverFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.UUID;

@Configuration
@AutoConfigureAfter(ApplicationConfiguration.class)
public class JCacheConfiguration {

    @Bean
    protected Cache<UUID, InvocationContext> invocationContextCache(
            @Nonnull CacheManager cacheManager,
            @Nonnull javax.cache.configuration.Configuration<UUID, InvocationContext> configuration) {
        return cacheManager.createCache("invocationContextCache", configuration);
    }

    @Bean
    protected Cache<UUID, VariableResolverFactory> variableResolverFactoryCache(
            @Nonnull CacheManager cacheManager,
            @Nonnull javax.cache.configuration.Configuration<UUID, VariableResolverFactory> configuration) {
        return cacheManager.createCache("variableResolverFactoryCache", configuration);
    }
}
