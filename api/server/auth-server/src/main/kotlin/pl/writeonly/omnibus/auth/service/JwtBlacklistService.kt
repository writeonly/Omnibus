package pl.writeonly.omnibus.auth.service
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class JwtBlacklistService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private fun key(jti: String) = "blacklist:$jti"

    fun revoke(jti: String, ttlSeconds: Long) {
        redisTemplate.opsForValue().set(key(jti), "true", Duration.ofSeconds(ttlSeconds))
    }

    fun isRevoked(jti: String): Boolean {
        return redisTemplate.hasKey(key(jti)) == true
    }
}
