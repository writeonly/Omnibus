import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxRelayService(
    private val repo: OutboxRepository
) {

    @Transactional
    fun fetchBatch(): List<OutboxEvent> {
        return repo.findUnpublished(limit = 50)
    }

    fun markPublished(event: OutboxEvent) {
        repo.markPublished(event.id)
    }

    fun markFailed(event: OutboxEvent, error: String) {
        repo.markFailed(event.id, error)
    }
}
