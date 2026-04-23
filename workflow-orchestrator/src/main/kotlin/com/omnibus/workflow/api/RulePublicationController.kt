package com.omnibus.workflow.api

import com.omnibus.workflow.application.RulePublicationWorkflowService
import com.omnibus.workflow.domain.RulePublicationRequest
import com.omnibus.workflow.domain.RulePublicationSubmission
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/rule-publications")
class RulePublicationController(
    private val rulePublicationWorkflowService: RulePublicationWorkflowService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun startPublication(@Valid @RequestBody request: RulePublicationRequest): Mono<RulePublicationSubmission> =
        rulePublicationWorkflowService.startPublication(request)
}

