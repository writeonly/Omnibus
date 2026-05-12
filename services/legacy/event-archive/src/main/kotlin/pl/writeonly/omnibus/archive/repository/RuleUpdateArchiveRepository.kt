package pl.writeonly.omnibus.archive.repository

import pl.writeonly.omnibus.archive.model.RuleUpdateByDay
import pl.writeonly.omnibus.archive.model.RuleUpdateByDayKey
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository

interface RuleUpdateArchiveRepository : ReactiveCassandraRepository<RuleUpdateByDay, RuleUpdateByDayKey>

