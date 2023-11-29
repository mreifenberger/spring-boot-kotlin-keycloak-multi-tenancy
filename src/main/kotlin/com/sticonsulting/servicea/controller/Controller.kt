package com.sticonsulting.servicea.controller

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/{realm}/api")
@CrossOrigin("*")
class Controller {

    @GetMapping("/hello")
    fun helloWorld(
        @PathVariable realm: String,
        @RequestParam(required = false) code: String?,
        @RequestParam(required = false) state: String?,
        jwt: Jwt?
    ): String {
        if (code != null && state != null) {
            // Handle OAuth2 callback logic here
            return "OAuth2 callback processing for realm: $realm"
        }

        // Regular request processing
        jwt ?: return "Unauthorized access"
        return "Hello from Service A"
    }
}
