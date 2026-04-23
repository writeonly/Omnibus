package pl.writeonly.omnibus.bidding.application

import pl.writeonly.omnibus.bidding.domain.ManagedRuleDefinition
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

@Component
class ManagedRuleStore(
    @Value("\${omnibus.rules.storage-path:managed-rules}") storagePath: String,
) {
    private val storageDirectory: Path = Path.of(storagePath)

    init {
        ensureDirectoryExists()
    }

    fun listManagedRules(): List<ManagedRuleDefinition> {
        ensureDirectoryExists()

        return try {
            Files.list(storageDirectory).use { paths ->
                paths
                    .filter { it.fileName.toString().endsWith(".drl") }
                    .sorted(compareBy { it.fileName.toString() })
                    .map { readManagedRule(it) }
                    .toList()
            }
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to list managed rules", exception)
        }
    }

    fun save(requestedName: String, content: String): ManagedRuleDefinition {
        ensureDirectoryExists()
        val fileName = sanitize(requestedName)
        val targetPath = storageDirectory.resolve(fileName)

        return try {
            Files.writeString(
                targetPath,
                content.trim() + System.lineSeparator(),
                StandardCharsets.UTF_8,
            )
            readManagedRule(targetPath)
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to save managed rule", exception)
        }
    }

    fun delete(fileName: String) {
        try {
            Files.deleteIfExists(storageDirectory.resolve(fileName))
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to delete managed rule after failed validation", exception)
        }
    }

    private fun readManagedRule(path: Path): ManagedRuleDefinition =
        try {
            ManagedRuleDefinition(
                path.fileName.toString(),
                "src/main/resources/rules/managed/${path.fileName}",
                true,
                Files.readString(path, StandardCharsets.UTF_8),
            )
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to read managed rule ${path.fileName}", exception)
        }

    private fun ensureDirectoryExists() {
        try {
            Files.createDirectories(storageDirectory)
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to initialize managed rules directory", exception)
        }
    }

    private fun sanitize(requestedName: String): String {
        val normalized = requestedName.trim().lowercase().replace(Regex("[^a-z0-9-_]+"), "-")
        if (normalized.isBlank()) {
            throw IllegalArgumentException("Rule name must contain at least one visible character")
        }
        return if (normalized.endsWith(".drl")) normalized else "$normalized.drl"
    }
}

