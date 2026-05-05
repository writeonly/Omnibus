package pl.writeonly.omnibus.archive.repository

import pl.writeonly.omnibus.archive.model.BffHttpEventByDay
import pl.writeonly.omnibus.archive.model.BffHttpEventByDayKey
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository

interface BffHttpEventArchiveRepository : ReactiveCassandraRepository<BffHttpEventByDay, BffHttpEventByDayKey>
