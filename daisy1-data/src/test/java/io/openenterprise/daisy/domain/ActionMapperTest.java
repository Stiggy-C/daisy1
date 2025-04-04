package io.openenterprise.daisy.domain;

import com.trivago.triava.tcache.EvictionPolicy;
import com.trivago.triava.tcache.core.Builder;
import io.openenterprise.commons.util.UUIDUtils;
import io.openenterprise.daisy.commons.context.ApplicationContextUtils;
import io.openenterprise.daisy.data.jpa.domain.Action;
import io.openenterprise.daisy.mvel2.Mvel2Operation;
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl;
import io.openenterprise.daisy.mvel2.service.Mvel2EvaluationService;
import io.openenterprise.daisy.mvel2.service.Mvel2EvaluationServiceImpl;
import io.openenterprise.daisy.springframework.boot.autoconfigure.cache.TCacheConfiguration;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mvel2.ParserConfiguration;
import org.mvel2.integration.VariableResolverFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@Import({ActionMapperTest.Configuration.class})
class ActionMapperTest {

    @Test
    void testEntityToModel() throws Exception {
        var entity = new Action();
        entity.setId(UUIDUtils.randomUUIDv7());
        entity.setOperation("io.openenterprise.daisy.mvel2.Mvel2OperationImpl");
        entity.setParameters(Map.of("daisy.mvel2.class-imports", Collections.<String>emptyList()));

        var model = ActionMapper.INSTANCE.entityToModel(entity);
        Assertions.assertNotNull(model);
        Assertions.assertNotNull(model.operation);
        Assertions.assertNotNull(model.parameters);
        Assertions.assertEquals(1, model.parameters.size());
    }

    @Test
    void testModelToEntity() {
        var parametersValuesMap = Map.<io.openenterprise.daisy.Parameter, Object>of(
                io.openenterprise.daisy.mvel2.Parameter.MVEL_CLASS_IMPORTS, Collections.emptyList());

        var model = new io.openenterprise.daisy.domain.Action<>();
        model.operation = new Mvel2OperationImpl<>();
        model.parameters = parametersValuesMap;

        var entity = ActionMapper.INSTANCE.modelToEntity(model);

        Assertions.assertNotNull(entity);
        Assertions.assertNotNull(entity.getOperation());
        Assertions.assertEquals("io.openenterprise.daisy.mvel2.Mvel2OperationImpl", entity.getOperation());
        Assertions.assertNotNull(entity.getParameters());
        Assertions.assertEquals(1, entity.getParameters().size());
    }

    @TestConfiguration
    protected static class Configuration {

        @Bean
        protected ApplicationContextUtils applicationContextUtils() {
            return new ApplicationContextUtils();
        }

        @Bean
        @Qualifier("builtInMvelVariables")
        protected Map<String, ?> builtInMvelVariables() {
            return Map.of();
        }

        @Bean
        protected Mvel2EvaluationService mvel2EvaluationService() {
            return new Mvel2EvaluationServiceImpl();
        }

        @Bean
        protected Mvel2Operation<?> mvel2Operation() {
            return new Mvel2OperationImpl<>();
        }

        @Bean
        protected ParserConfiguration parserConfiguration() {
            return new ParserConfiguration();
        }

        @Bean
        protected Cache<java.util.UUID, org.mvel2.integration.VariableResolverFactory>
                variableResolverFactoryCache() {
            return Mockito.mock(Cache.class);
        }
    }
}
