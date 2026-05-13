package pl.writeonly.omnibus.workflow.api

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    @Bean
    fun workflowOpenApi(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("Omnibus Workflow Engine API")
                .version("v1")
                .description("Camunda-backed workflows around Drools rule governance"),
        )
}

