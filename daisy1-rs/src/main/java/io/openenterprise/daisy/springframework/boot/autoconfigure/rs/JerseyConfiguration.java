package io.openenterprise.daisy.springframework.boot.autoconfigure.rs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.openenterprise.daisy.rs.ActionsApiService;
import io.openenterprise.daisy.rs.ActionsChainApiService;
import jakarta.annotation.PostConstruct;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

@Configuration
@ComponentScan("io.openenterprise.daisy.rs")
public class JerseyConfiguration extends ResourceConfig {

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @PostConstruct
    protected void postConstruct() {
        var actionsApiService = applicationContext.getBean(ActionsApiService.class);
        var actionsChainApiService = applicationContext.getBean(ActionsChainApiService.class);
        var jacksonFeatures = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);

        registerInstances(actionsApiService, actionsChainApiService, jacksonFeatures);
    }
}
