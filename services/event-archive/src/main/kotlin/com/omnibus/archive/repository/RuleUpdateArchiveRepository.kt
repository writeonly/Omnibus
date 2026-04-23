package com.omnibus.archive.repository

import com.omnibus.archive.model.RuleUpdateByDay
import com.omnibus.archive.model.RuleUpdateByDayKey
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository

interface RuleUpdateArchiveRepository : ReactiveCassandraRepository<RuleUpdateByDay, RuleUpdateByDayKey>

