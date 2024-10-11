package io.openenterprise.daisy.springframework.boot.autoconfigure.spark;

import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(SparkSession.class)
@ConditionalOnMissingBean(SparkSession.class)
public class SparkConfiguration {

    @Value("${spark.master}")
    protected String sparkMaster;

    @Bean
    protected SparkSession sparkSession() {
        return SparkSession.builder().remote(sparkMaster).build();
    }
}
