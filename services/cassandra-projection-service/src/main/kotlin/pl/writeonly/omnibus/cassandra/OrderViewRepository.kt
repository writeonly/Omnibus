import org.springframework.data.cassandra.repository.CassandraRepository

interface OrderViewRepository : CassandraRepository<OrderView, String>
