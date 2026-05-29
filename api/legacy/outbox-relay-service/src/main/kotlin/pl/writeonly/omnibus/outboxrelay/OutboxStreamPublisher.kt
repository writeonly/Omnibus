package pl.writeonly.omnibus.outboxrelay

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxStreamPublisher(
    private val service: OutboxRelayService,
    private val streamBridge: StreamBridge,
    private val objectMapper: ObjectMapper,
) {
    private val payloadType = object : TypeReference<Map<String, Any?>>() {}

    @Scheduled(fixedDelayString = "\${outbox.relay.fixed-delay:1000}")
    fun publishPendingEvents() {
        service.fetchBatch().forEach { event ->
            try {
                val payload = objectMapper.readValue(event.payload, payloadType)
                val sent = streamBridge.send(
                    "outboxEvents-out-0",
                    MessageBuilder.withPayload(payload)
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
