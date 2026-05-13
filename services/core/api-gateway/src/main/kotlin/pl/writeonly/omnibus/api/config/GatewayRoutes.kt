package pl.writeonly.omnibus.api.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayRoutes {

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()

            .route("user-service") { r ->
                r.path("/users/**")
                    .filters { f ->
                        f.stripPrefix(1)
                            .addRequestHeader("X-Service", "user-service")
                    }
                    .uri("http://localhost:8081")
            }

            .route("order-service") { r ->
                r.path("/orders/**")
                    .filters { f ->
                        f.stripPrefix(1)
                            .addRequestHeader("X-Service", "order-service")
                    }
                    .uri("http://localhost:8082")
            }

            .route("payment-service") { r ->
                r.path("/payments/**")
                    .filters { f ->
                        f.stripPrefix(1)
                    }
                    .uri("http://localhost:8083")
            }

            .build()
    }
}
