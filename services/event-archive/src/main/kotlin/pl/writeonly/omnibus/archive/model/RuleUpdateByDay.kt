package pl.writeonly.omnibus.archive.model

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("rule_updates_by_day")
data class RuleUpdateByDay(
    @PrimaryKey
    val key: RuleUpdateByDayKey,
    val occurredAt: Instant,
    val ruleName: String,
    val sourcePath: String,
    val managed: Boolean,
    val contentLength: Int,
)

