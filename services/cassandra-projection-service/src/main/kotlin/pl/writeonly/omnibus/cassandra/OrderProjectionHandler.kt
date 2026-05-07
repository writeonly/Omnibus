import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class OrderProjectionHandler(
    private val repository: OrderViewRepository
) {

    @Bean
    fun orders(): Consumer<OrderEvent> {
        return Consumer { event ->

            val view = OrderView(
                orderId = event.orderId,
                status = event.status,
                payload = event.payload
            )

            repository.save(view)
        }
    }
}
