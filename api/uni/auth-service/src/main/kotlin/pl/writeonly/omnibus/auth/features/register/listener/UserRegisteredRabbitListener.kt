package pl.writeonly.omnibus.auth.features.register.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pl.writeonly.omnibus.auth.features.register.UserRegisteredEvent
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@Component
@ConditionalOnProperty(name = ["messaging.provider"], havingValue = "rabbit")
class UserRegisteredRabbitListener(
    private val rabbitTemplate: org.springframework.amqp.rabbit.core.RabbitTemplate
) {

    @EventListener
    fun handle(event: UserRegisteredEvent) {
        rabbitTemplate.convertAndSend("user.exchange", "user.registered", event)
    }
}