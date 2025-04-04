package io.openenterprise.test.junit.jupiter;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainerProvider;

public class PostgresExtension implements BeforeAllCallback, AfterAllCallback {

    protected static final JdbcDatabaseContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainerProvider().newInstance("15.10");


    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        POSTGRES_CONTAINER.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        POSTGRES_CONTAINER.start();

        System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRES_CONTAINER.getPassword());
    }
}
