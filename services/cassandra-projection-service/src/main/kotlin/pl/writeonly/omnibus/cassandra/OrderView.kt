import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("orders")
data class OrderView(
    @PrimaryKey
    val orderId: String,

    val status: String,
    val payload: String
)
