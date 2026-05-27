package pl.writeonly.omnibus.modulith

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled
import org.springframework.modulith.core.ApplicationModules

internal class ModularityTests {

    @Disabled
    @Test
    fun verifyModules() {
        ApplicationModules.of(ModulithApplication::class.java)
            .verify()
    }
}