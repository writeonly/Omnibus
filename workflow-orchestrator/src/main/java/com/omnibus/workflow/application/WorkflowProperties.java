package com.omnibus.workflow.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "omnibus.workflow")
public record WorkflowProperties(
    String biddingEngineBaseUrl
) {
}
