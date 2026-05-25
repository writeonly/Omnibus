package pl.writeonly.omnibus.auth.component
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pl.writeonly.omnibus.auth.service.JwtBlacklistService

@Component
class JwtBlacklistFilter(
    private val blacklistService: JwtBlacklistService,
    private val jwtDecoder: JwtDecoder
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val auth = request.getHeader("Authorization")

        if (auth != null && auth.startsWith("Bearer ")) {
            val token = auth.removePrefix("Bearer ").trim()

            val jwt = jwtDecoder.decode(token)

            val jti = jwt.id

            if (jti != null && blacklistService.isRevoked(jti)) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
