package com.sticonsulting.servicea.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("{realm}/public/api")
class PublicController {

    @GetMapping("/hello")
    fun helloUnsecoredServiceA(): String {
        return "Hello unsecured Service A"
    }

}