package com.omnibus.archive.repository;

import com.omnibus.archive.model.RecommendationByDay;
import com.omnibus.archive.model.RecommendationByDayKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface RecommendationArchiveRepository
    extends CassandraRepository<RecommendationByDay, RecommendationByDayKey> {
}
