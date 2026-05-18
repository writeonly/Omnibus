package pl.writeonly.omnibus.archive.repository

import pl.writeonly.omnibus.archive.model.RecommendationByDay
import pl.writeonly.omnibus.archive.model.RecommendationByDayKey
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository

interface RecommendationArchiveRepository : ReactiveCassandraRepository<RecommendationByDay, RecommendationByDayKey>

