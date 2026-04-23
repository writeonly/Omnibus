package pl.writeonly.omnibus.workflow.application

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "omnibus.workflow")
data class WorkflowProperties(
    val biddingEngineBaseUrl: String,
)

