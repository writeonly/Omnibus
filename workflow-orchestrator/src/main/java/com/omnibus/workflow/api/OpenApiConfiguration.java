package com.omnibus.workflow.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    OpenAPI workflowOpenApi() {
        return new OpenAPI().info(new Info()
            .title("Omnibus Workflow Orchestrator API")
            .version("v1")
            .description("Camunda-backed workflows around Drools rule governance"));
    }
}
