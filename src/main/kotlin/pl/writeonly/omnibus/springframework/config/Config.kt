package pl.writeonly.omnibus.springframework.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration

@Configuration
class Config(val objectMapper: ObjectMapper) {

    fun customizeObjectMapper() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}
