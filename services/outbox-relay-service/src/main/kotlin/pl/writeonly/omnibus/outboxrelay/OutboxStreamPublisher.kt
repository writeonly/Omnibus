package pl.writeonly.omnibus.outboxrelay

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Supplier

@Component
class OutboxStreamPublisher(
    private val service: OutboxRelayService
) {

    @Bean
    fun outboxEvents(): Supplier<List<OutboxEvent>> {
        return Supplier {
            service.fetchBatch()
        }
    }
}
