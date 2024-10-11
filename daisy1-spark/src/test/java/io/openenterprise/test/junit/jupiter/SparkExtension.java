package io.openenterprise.test.junit.jupiter;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.FixedHostPortGenericContainer;

public class SparkExtension implements BeforeAllCallback, AfterAllCallback {

    protected static final String SPARK_DOCKER_IMAGE_TAG = "daisy/spark:4.0.0-preview2";

    @SuppressWarnings("deprecation")
    protected static final FixedHostPortGenericContainer<?> SPARK_CONTAINER = new FixedHostPortGenericContainer<>(
            SPARK_DOCKER_IMAGE_TAG);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        SPARK_CONTAINER.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!SPARK_CONTAINER.isRunning()) {
            SPARK_CONTAINER//.withExposedPorts(4040, 4041, 7077, 8080, 8081, 10000, 15002)
                    .withFixedExposedPort(4040, 4040)
                    .withFixedExposedPort(4041, 4041)
                    .withFixedExposedPort(7077, 7077)
                    .withFixedExposedPort(8080, 8080)
                    .withFixedExposedPort(10000, 10000)
                    .withFixedExposedPort(15002, 15002);
        }

        SPARK_CONTAINER.start();

        System.setProperty("spark.master", "sc://" + SPARK_CONTAINER.getHost());
    }
}
