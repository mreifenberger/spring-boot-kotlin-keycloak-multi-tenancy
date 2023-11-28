package com.sticonsulting.servicea.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class Controller {

    @GetMapping("/hello")
    fun helloWorld(): String {
        return "Hello from Service A"
    }
}