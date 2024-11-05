package io.openenterprise.daisy;

import com.google.common.collect.Lists;
import io.openenterprise.daisy.domain.Parameter;
import io.openenterprise.daisy.mvel2.Mvel2Operation;
import io.openenterprise.daisy.mvel2.Mvel2OperationImpl;
import io.openenterprise.daisy.service.Mvel2EvaluationService;
import io.openenterprise.daisy.service.Mvel2EvaluationServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
@ComponentScan("io.openenterprise.daisy1.springframework.boot.autoconfigure")
@Import(RunnerImplTest.Configuration.class)
class RunnerImplTest {

    @Autowired
    protected Mvel2Operation<?> mvel2Operation;

    @Autowired
    protected Runner runner;

    @Test
    void testRunWithInvocationContext() {
        var parameters0 = new Parameters();
        parameters0.put(Parameter.MVEL_CLASS_IMPORTS, new String[]{});
        parameters0.put(Parameter.MVEL_EXPRESSIONS, new String[]{"a = 0; a;"});
        parameters0.put(Parameter.MVEL_PACKAGE_IMPORTS, new String[]{});

        var parameters1 = new Parameters();
        parameters1.put(Parameter.MVEL_CLASS_IMPORTS, new String[]{});
        parameters1.put(Parameter.MVEL_EXPRESSIONS, new String[]{"b = 1; a + b;"});
        parameters1.put(Parameter.MVEL_PACKAGE_IMPORTS, new String[]{});

        var invocationContext = new InvocationContext();
        var operationsAndParameters = Lists.<Pair<Operation<?>, Parameters>>newArrayList(Pair.of(mvel2Operation,
                parameters0), Pair.of(mvel2Operation, parameters1));

        runner.run(invocationContext, operationsAndParameters);

        Assertions.assertFalse(CollectionUtils.isEmpty(invocationContext.previousInvocations));
        Assertions.assertTrue(invocationContext.previousInvocations.stream()
                .noneMatch(invocation -> Objects.isNull(invocation.getResult())));
        Assertions.assertEquals(1, invocationContext.previousInvocations.getLast().result);
    }

    @TestConfiguration
    protected static class Configuration {

        @Bean
        protected Mvel2Operation<?> mvel2Operation() {
            return new Mvel2OperationImpl<>();
        }

        @Bean
        protected Mvel2EvaluationService mvelExpressionEvaluationService() {
            return new Mvel2EvaluationServiceImpl();
        }

        @Bean
        protected Runner runner() {
            return new RunnerImpl();
        }
    }
}