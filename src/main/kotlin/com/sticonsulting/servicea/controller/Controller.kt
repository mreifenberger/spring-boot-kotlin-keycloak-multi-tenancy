package com.sticonsulting.servicea.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class Controller {

    @GetMapping("/hello")
    fun helloWorld(): Mono<String> {
        return Mono.just("Hello from Service A")
    }
}