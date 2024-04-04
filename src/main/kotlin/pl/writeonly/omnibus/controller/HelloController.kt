package pl.writeonly.omnibus.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.service.HelloService

@RestController
class HelloController(val helloService: HelloService) {

    @GetMapping("/")
    fun hello() = helloService.hello()
}