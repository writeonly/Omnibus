package pl.writeonly.omnibus.outboxrelay

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxStreamPublisher(
    private val service: OutboxRelayService,
    private val streamBridge: StreamBridge,
) {
    @Scheduled(fixedDelayString = "\${outbox.relay.fixed-delay:1000}")
    fun publishPendingEvents() {
        service.fetchBatch().forEach { event ->
            try {
                val sent = streamBridge.send(
                    "outboxEvents-out-0",
                    MessageBuilder.withPayload(event.payload)
                        .setHeader("eventType", event.eventType)
                        .setHeader("aggregateId", event.aggregateId)
                        .build(),
                )

                if (sent) {
                    service.markPublished(event)
                } else {
                    service.markFailed(event, "Spring Cloud Stream returned false")
                }
            } catch (exception: RuntimeException) {
                service.markFailed(event, exception.message ?: exception.javaClass.name)
            }
        }
    }
}
