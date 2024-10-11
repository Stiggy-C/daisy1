package io.openenterprise.daisy.springframework.boot.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import org.mvel2.ParserConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.cache.CacheManager;
import javax.cache.Caching;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
public class ApplicationConfiguration {

    @Bean("builtInMvelVariables")
    protected Map<String, Object> builtInMvelVariables(@Nonnull ApplicationContext applicationContext,
                                                       @Nonnull ObjectMapper objectMapper) {
        return Maps.newHashMap(Map.of("applicationContext", applicationContext, "objectMapper", objectMapper));
    }

    @Bean
    protected CacheManager cacheManager() {
        return Caching.getCachingProvider().getCacheManager();
    }

    @Bean
    protected ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    protected ParserConfiguration parserConfiguration() {
        var parserConfiguration = new ParserConfiguration();

        // Add common utils:
        parserConfiguration.addPackageImport("com.google.common.collect");
        parserConfiguration.addPackageImport("org.apache.commons.collections4");
        parserConfiguration.addPackageImport("org.apache.commons.lang3");

        return parserConfiguration;
    }
}
