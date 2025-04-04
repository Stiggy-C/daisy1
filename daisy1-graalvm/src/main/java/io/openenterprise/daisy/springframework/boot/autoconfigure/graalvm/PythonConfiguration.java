package io.openenterprise.daisy.springframework.boot.autoconfigure.graalvm;

import io.openenterprise.daisy.springframework.boot.autoconfigure.ApplicationConfiguration;
import io.openenterprise.graalvm.polyglot.ContextDelegate;
import io.openenterprise.graalvm.polyglot.PooledContextDelegateFactory;
import io.openenterprise.graalvm.polyglot.python.ContextBuilder;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(ApplicationConfiguration.class)
public class PythonConfiguration {

    @Bean("pythonContextBuilder")
    protected ContextBuilder pythonContextBuilder() {
        return new ContextBuilder();
    }

    @Bean("pythonPooledContextDelegateFactory")
    protected PooledContextDelegateFactory pythonPooledContextDelegateFactory(
            @Qualifier("pythonContextBuilder") ContextBuilder contextBuilder) {
        return new PooledContextDelegateFactory(contextBuilder);
    }

    @Bean("pythonContextDelegatePoolConfig")
    protected GenericObjectPoolConfig<ContextDelegate> pythonContextDelegatePoolConfig() {
        var genericObjectPoolConfig = new GenericObjectPoolConfig<ContextDelegate>();
        genericObjectPoolConfig.setFairness(true);
        genericObjectPoolConfig.setMinIdle(1);
        genericObjectPoolConfig.setMaxIdle(Runtime.getRuntime().availableProcessors());
        genericObjectPoolConfig.setMaxTotal(Runtime.getRuntime().availableProcessors());

        return genericObjectPoolConfig;
    }

    @Bean("pythonContextDelegatePool")
    protected ObjectPool<ContextDelegate> pythonContextDelegatePool(
            @Qualifier("pythonPooledContextDelegateFactory") PooledContextDelegateFactory pythonPooledContextDelegateFactory,
            @Qualifier("pythonContextDelegatePoolConfig") GenericObjectPoolConfig<ContextDelegate> pythonContextDelegatePoolConfig) {
        return new GenericObjectPool<>(pythonPooledContextDelegateFactory, pythonContextDelegatePoolConfig);
    }
}
