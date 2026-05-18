package pl.writeonly.omnibus.rule.api

import pl.writeonly.omnibus.rule.application.ManagedRuleAdminService
import pl.writeonly.omnibus.rule.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.rule.domain.ManagedRuleUpsertRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/rules")
class ManagedRuleController(
    private val managedRuleAdminService: ManagedRuleAdminService,
) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listRules(): List<ManagedRuleDefinition> = managedRuleAdminService.listRules()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveRule(@Valid @RequestBody request: ManagedRuleUpsertRequest): ManagedRuleDefinition =
        managedRuleAdminService.saveRule(request)
}
