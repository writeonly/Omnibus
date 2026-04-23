package com.omnibus.bidding.api;

import com.omnibus.bidding.application.ManagedRuleAdminService;
import com.omnibus.bidding.domain.ManagedRuleDefinition;
import com.omnibus.bidding.domain.ManagedRuleUpsertRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/rules")
public class ManagedRuleController {

    private final ManagedRuleAdminService managedRuleAdminService;

    public ManagedRuleController(ManagedRuleAdminService managedRuleAdminService) {
        this.managedRuleAdminService = managedRuleAdminService;
    }

    @GetMapping
    public List<ManagedRuleDefinition> listRules() {
        return managedRuleAdminService.listRules();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManagedRuleDefinition saveRule(@Valid @RequestBody ManagedRuleUpsertRequest request) {
        return managedRuleAdminService.saveRule(request);
    }
}
