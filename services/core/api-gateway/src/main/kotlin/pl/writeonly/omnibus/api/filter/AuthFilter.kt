package pl.writeonly.omnibus.api.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthFilter : GlobalFilter, Ordered {

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {

        val request = exchange.request
        val path = request.uri.path

        if (path.startsWith("/auth") || path.startsWith("/actuator")) {
            return chain.filter(exchange)
        }

        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }

        val token = authHeader.removePrefix("Bearer ")

        val isValid = validateToken(token)

        if (!isValid) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }

        val mutatedRequest = request.mutate()
            .header("X-User-Id", extractUserId(token))
            .build()

        val mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build()

        return chain.filter(mutatedExchange)
    }

    override fun getOrder(): Int = -1

    private fun validateToken(token: String): Boolean {
        return token.isNotBlank()
    }

    private fun extractUserId(token: String): String {
        return "123"
    }
}
