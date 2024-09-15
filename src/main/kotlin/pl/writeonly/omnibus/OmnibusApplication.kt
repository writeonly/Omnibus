package pl.writeonly.omnibus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OmnibusApplication {
	fun main(args: Array<String>) {
		runApplication<OmnibusApplication>(*args)
	}
}

fun main(args: Array<String>) {
	runApplication<OmnibusApplication>(*args)
}
