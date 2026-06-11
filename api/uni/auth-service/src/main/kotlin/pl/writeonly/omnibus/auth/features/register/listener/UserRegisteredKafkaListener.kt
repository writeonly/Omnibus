package pl.writeonly.omnibus.auth.features.register.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pl.writeonly.omnibus.auth.features.register.UserRegisteredEvent
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@Component
@ConditionalOnProperty(name = ["messaging.provider"], havingValue = "kafka")
class UserRegisteredKafkaListener(
    private val kafkaTemplate: org.springframework.kafka.core.KafkaTemplate<String, Any>
) {

    @EventListener
    fun handle(event: UserRegisteredEvent) {
        kafkaTemplate.send("user.registered", event.username, event)
    }
}