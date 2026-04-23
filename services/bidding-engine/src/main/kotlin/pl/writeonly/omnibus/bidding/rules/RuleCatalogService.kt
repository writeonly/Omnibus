package pl.writeonly.omnibus.bidding.rules

import com.omnibus.bidding.application.ManagedRuleStore
import com.omnibus.bidding.domain.ManagedRuleDefinition
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets

@Service
class RuleCatalogService(
    private val managedRuleStore: ManagedRuleStore,
) {
    private val resourcePatternResolver = PathMatchingResourcePatternResolver()

    fun listAllRules(): List<ManagedRuleDefinition> =
        buildList {
            addAll(loadBundledRules())
            addAll(managedRuleStore.listManagedRules())
        }

    fun listManagedRules(): List<ManagedRuleDefinition> = managedRuleStore.listManagedRules()

    private fun loadBundledRules(): List<ManagedRuleDefinition> =
        try {
            val resources = resourcePatternResolver.getResources("classpath*:rules/*.drl")
            resources.map { resource ->
                val fileName = resource.filename
                    ?: throw IllegalStateException("Unable to read bundled rules (missing filename)")
                ManagedRuleDefinition(
                    fileName,
                    "src/main/resources/rules/$fileName",
                    false,
                    String(resource.inputStream.readAllBytes(), StandardCharsets.UTF_8),
                )
            }
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to read bundled rules", exception)
        }
}

