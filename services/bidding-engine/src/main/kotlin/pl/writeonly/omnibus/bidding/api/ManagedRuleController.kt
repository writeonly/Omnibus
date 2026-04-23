package pl.writeonly.omnibus.bidding.api

import com.omnibus.bidding.application.ManagedRuleAdminService
import com.omnibus.bidding.domain.ManagedRuleDefinition
import com.omnibus.bidding.domain.ManagedRuleUpsertRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/admin/rules")
class ManagedRuleController(
    private val managedRuleAdminService: ManagedRuleAdminService,
) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listRules(): Flux<ManagedRuleDefinition> = managedRuleAdminService.listRules()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveRule(@Valid @RequestBody request: ManagedRuleUpsertRequest): Mono<ManagedRuleDefinition> =
        managedRuleAdminService.saveRule(request)
}

