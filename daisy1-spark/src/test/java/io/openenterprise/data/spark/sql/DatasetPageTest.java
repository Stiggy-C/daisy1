package io.openenterprise.data.spark.sql;

import io.openenterprise.daisy.data.spark.sql.DatasetPage;
import io.openenterprise.test.junit.jupiter.SparkExtension;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.spark.sql.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

@ExtendWith(value = {SparkExtension.class, SpringExtension.class})
@ComponentScan(basePackages = {"io.openenterprise.daisy.springframework.boot.autoconfigure",
        "io.openenterprise.daisy.springframework.boot.autoconfigure.spark"})
class DatasetPageTest {

    protected Dataset<Row> dataset;

    @Test
    void test() {
        var datasetPage = Assertions.assertDoesNotThrow(() -> new DatasetPage(dataset, PageRequest.of(0,
                32, Sort.Direction.ASC, "id")));

        Assertions.assertNotNull(datasetPage);
        Assertions.assertNotNull(datasetPage.getContent());
        Assertions.assertEquals(32, datasetPage.getContent().size());
        Assertions.assertTrue(datasetPage.hasContent());
        Assertions.assertTrue(datasetPage.hasNext());
        Assertions.assertFalse(datasetPage.hasPrevious());
        Assertions.assertTrue(datasetPage.isFirst());
        Assertions.assertFalse(datasetPage.isLast());
        Assertions.assertNotNull(datasetPage.nextPageable());
        Assertions.assertEquals(datasetPage.getNumber() + 1, datasetPage.nextPageable().getPageNumber());
        Assertions.assertEquals(datasetPage.getNumber(), datasetPage.previousPageable().getPageNumber());
    }

    @PostConstruct
    void postConstruct() {
        var dummies = IntStream.range(0, 64)
                .mapToObj(i -> new DummyData(i, RandomStringUtils.randomAlphanumeric(0, 32)))
                .toList();

        dataset = SparkSession.builder().remote("sc://localhost").getOrCreate()
                .createDataFrame(dummies, DummyData.class);
    }

    @TestConfiguration
    protected static class Configuration {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class DummyData {

        protected long id;

        protected String text;

    }
}
