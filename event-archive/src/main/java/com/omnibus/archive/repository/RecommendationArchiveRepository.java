package com.omnibus.archive.repository;

import com.omnibus.archive.model.RecommendationByDay;
import com.omnibus.archive.model.RecommendationByDayKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface RecommendationArchiveRepository
    extends ReactiveCassandraRepository<RecommendationByDay, RecommendationByDayKey> {
}
