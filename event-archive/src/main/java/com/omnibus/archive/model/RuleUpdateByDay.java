package com.omnibus.archive.model;

import java.time.Instant;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("rule_updates_by_day")
public class RuleUpdateByDay {

    @PrimaryKey
    private RuleUpdateByDayKey key;

    private Instant occurredAt;
    private String ruleName;
    private String sourcePath;
    private Boolean managed;
    private Integer contentLength;

    public RuleUpdateByDay() {
    }

    public RuleUpdateByDay(
        RuleUpdateByDayKey key,
        Instant occurredAt,
        String ruleName,
        String sourcePath,
        Boolean managed,
        Integer contentLength
    ) {
        this.key = key;
        this.occurredAt = occurredAt;
        this.ruleName = ruleName;
        this.sourcePath = sourcePath;
        this.managed = managed;
        this.contentLength = contentLength;
    }

    public RuleUpdateByDayKey getKey() {
        return key;
    }
}
