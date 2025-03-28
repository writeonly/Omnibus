package pl.writeonly.omnibus.service

import org.springframework.stereotype.Service

@Service
class HelloService {
    fun hello() = HELLO_MESSAGE

    companion object {
        const val HELLO_MESSAGE = "Hello, World!"
    }
}
