package pl.writeonly.omnibus.bidding.api

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    @Bean
    fun omnibusOpenApi(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("Omnibus Bidding Engine API")
                .description("REST API for bridge bidding recommendations and Drools rule administration.")
                .version("v1")
                .contact(
                    Contact()
                        .name("Omnibus")
                        .url("https://example.local/omnibus"),
                )
                .license(
                    License()
                        .name("Internal Use"),
                ),
        )
}

