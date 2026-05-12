package pl.writeonly.omnibus.archive.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KafkaTopicsProperties::class)
class ArchiveConfiguration

