package com.omnibus.bidding.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI omnibusOpenApi() {
        return new OpenAPI().info(new Info()
            .title("Omnibus Bidding Engine API")
            .description("REST API for bridge bidding recommendations and Drools rule administration.")
            .version("v1")
            .contact(new Contact()
                .name("Omnibus")
                .url("https://example.local/omnibus"))
            .license(new License()
                .name("Internal Use")));
    }
}
