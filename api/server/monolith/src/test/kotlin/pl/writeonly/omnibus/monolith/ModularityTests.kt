package pl.writeonly.omnibus.monolith

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled
import org.springframework.modulith.core.ApplicationModules

internal class ModularityTests {
    
    @Disabled
    @Test
    fun verifyModules() {
        ApplicationModules.of(MonolithApplication::class.java)
            .verify()
    }
}