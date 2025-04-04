package io.openenterprise.daisy.data.jpa.service;

import io.openenterprise.daisy.data.jpa.domain.Action;
import io.openenterprise.test.junit.jupiter.PostgresExtension;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ExtendWith({PostgresExtension.class, SpringExtension.class})
@EnableAutoConfiguration
@EntityScan("io.openenterprise.daisy.data.jpa.domain")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@EnableJpaAuditing
@EnableJpaRepositories("io.openenterprise.daisy.data.jpa.repository")
@EnableTransactionManagement
@Import(ActionServiceTest.Configuration.class)
@ContextConfiguration(classes = ActionServiceTest.class)
class ActionServiceTest {

    @Autowired
    protected ActionService actionService;

    @Autowired
    protected EntityManager entityManager;

    @Test
    void testSave() {
        var action = new Action();

        action.setOperation("io.openenterprise.daisy.mvel2.Mvel2Operation");

        TestTransaction.start();
        Assertions.assertTrue(TestTransaction.isActive());

        TestTransaction.flagForCommit();
        Assertions.assertFalse(TestTransaction.isFlaggedForRollback());

        var saved = actionService.save(action);

        TestTransaction.end();

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertNotNull(saved.getCreatedInstant());

        TestTransaction.start();
        Assertions.assertTrue(TestTransaction.isActive());

        var entity = entityManager.find(Action.class, saved.getId());

        TestTransaction.end();

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(saved.getId(), entity.getId());
    }

    @TestConfiguration
    protected static class Configuration {

        @Bean
        protected ActionService actionService() {
            return new ActionServiceImpl();
        }

    }

}
