package com.omnibus.bidding.application;

import com.omnibus.bidding.domain.ManagedRuleDefinition;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ManagedRuleStore {

    private final Path storageDirectory;

    public ManagedRuleStore(@Value("${omnibus.rules.storage-path:managed-rules}") String storagePath) {
        this.storageDirectory = Path.of(storagePath);
        ensureDirectoryExists();
    }

    public List<ManagedRuleDefinition> listManagedRules() {
        ensureDirectoryExists();

        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return paths
                .filter(path -> path.getFileName().toString().endsWith(".drl"))
                .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                .map(this::readManagedRule)
                .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to list managed rules", exception);
        }
    }

    public ManagedRuleDefinition save(String requestedName, String content) {
        ensureDirectoryExists();
        String fileName = sanitize(requestedName);
        Path targetPath = storageDirectory.resolve(fileName);

        try {
            Files.writeString(targetPath, content.trim() + System.lineSeparator(), StandardCharsets.UTF_8);
            return readManagedRule(targetPath);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save managed rule", exception);
        }
    }

    public void delete(String fileName) {
        try {
            Files.deleteIfExists(storageDirectory.resolve(fileName));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to delete managed rule after failed validation", exception);
        }
    }

    private ManagedRuleDefinition readManagedRule(Path path) {
        try {
            return new ManagedRuleDefinition(
                path.getFileName().toString(),
                "src/main/resources/rules/managed/" + path.getFileName(),
                true,
                Files.readString(path, StandardCharsets.UTF_8)
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read managed rule " + path.getFileName(), exception);
        }
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to initialize managed rules directory", exception);
        }
    }

    private String sanitize(String requestedName) {
        String normalized = requestedName.trim().toLowerCase().replaceAll("[^a-z0-9-_]+", "-");
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Rule name must contain at least one visible character");
        }
        return normalized.endsWith(".drl") ? normalized : normalized + ".drl";
    }
}
