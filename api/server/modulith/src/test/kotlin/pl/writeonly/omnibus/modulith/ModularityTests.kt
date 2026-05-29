package pl.writeonly.omnibus.modulith

import io.kotest.core.spec.style.FreeSpec
import org.springframework.modulith.core.ApplicationModules

internal class ModularityTests : FreeSpec({
    "modulith modules are valid".config(enabled = false) {
        ApplicationModules.of(ModulithApplication::class.java)
            .verify()
    }
})
