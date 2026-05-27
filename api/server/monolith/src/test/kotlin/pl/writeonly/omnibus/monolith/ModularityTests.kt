package pl.writeonly.omnibus.monolith

import com.apple.eawt.Application
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules


internal class ModularityTests {
    @Test
    fun verifyModules() {
        ApplicationModules.of(Application::class.java)
            .verify()
    }
}