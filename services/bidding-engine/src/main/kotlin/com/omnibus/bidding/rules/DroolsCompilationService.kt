package com.omnibus.bidding.rules

import com.omnibus.bidding.domain.ManagedRuleDefinition
import org.kie.api.KieServices
import org.kie.api.builder.Message
import org.kie.api.runtime.KieContainer
import org.springframework.stereotype.Service

@Service
class DroolsCompilationService {
    fun buildContainer(ruleDefinitions: List<ManagedRuleDefinition>): KieContainer {
        val kieServices = KieServices.Factory.get()
        val kieFileSystem = kieServices.newKieFileSystem()

        for (ruleDefinition in ruleDefinitions) {
            kieFileSystem.write(ruleDefinition.sourcePath, ruleDefinition.content)
        }

        val kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll()
        val hasErrors = kieBuilder.results.hasMessages(Message.Level.ERROR)
        if (hasErrors) {
            throw IllegalArgumentException(kieBuilder.results.toString())
        }

        return kieServices.newKieContainer(kieServices.repository.defaultReleaseId)
    }
}

