package pl.writeonly.omnibus.workflow.api

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.workflow.application.RulePublicationWorkflowService
import pl.writeonly.omnibus.workflow.domain.RulePublicationRequest
import pl.writeonly.omnibus.workflow.domain.RulePublicationSubmission

@RestController
@RequestMapping("/api/v1/rule-publications")
class RulePublicationController(
    private val rulePublicationWorkflowService: RulePublicationWorkflowService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun startPublication(@Valid @RequestBody request: RulePublicationRequest): RulePublicationSubmission =
        rulePublicationWorkflowService.startPublication(request)
}
