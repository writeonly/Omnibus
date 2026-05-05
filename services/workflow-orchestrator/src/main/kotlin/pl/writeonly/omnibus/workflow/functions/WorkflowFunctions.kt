package pl.writeonly.omnibus.workflow.functions

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.writeonly.omnibus.workflow.application.RulePublicationWorkflowService
import pl.writeonly.omnibus.workflow.domain.RulePublicationRequest
import pl.writeonly.omnibus.workflow.domain.RulePublicationSubmission
import java.util.function.Function

@Configuration
class WorkflowFunctions {
    @Bean
    fun startRulePublication(
        rulePublicationWorkflowService: RulePublicationWorkflowService,
    ): Function<RulePublicationRequest, RulePublicationSubmission> =
        Function { request -> rulePublicationWorkflowService.startPublication(request) }
}
