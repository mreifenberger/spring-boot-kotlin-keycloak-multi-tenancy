package com.sticonsulting.servicea.config

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class SubdomainAuthEntryPointWrapper(private val location: String) : ServerAuthenticationEntryPoint {

    companion object {
        val PREFIXES = arrayOf("/_nuxt", "/__webpack_hmr")
        val SUFFIXES = arrayOf(".js", ".png", ".css", ".ico", ".ttf", ".woff")
    }

    override fun commence(exchange: ServerWebExchange?, e: AuthenticationException?): Mono<Void> {
        return Mono.fromRunnable {
            val response = exchange!!.response
            val request = exchange.request
            println("My original url is: ${request.uri}")
            val origin = if (request.queryParams.getFirst("originUrl") == null && saveOriginUrl(request.uri)) {
                "?originUrl=" + URLEncoder.encode(request.uri.toASCIIString(), StandardCharsets.UTF_8)
            } else ""
            println("My new origin url is: $origin")
            response.statusCode = HttpStatus.FOUND
            response.headers.location = createLocation(exchange, location + origin)
        }
    }

    private fun saveOriginUrl(uri: URI): Boolean {
        val path = uri.path.lowercase()
        for (prefix in PREFIXES) {
            if (path.startsWith(prefix)) {
                return false
            }
        }
        for (suffix in SUFFIXES) {
            if (path.endsWith(suffix)) {
                return false
            }
        }
        return true
    }

    private fun createLocation(exchange: ServerWebExchange, location: String): URI {
        val url = location
        if (url.startsWith("/")) {
            val context = exchange.request.path.contextPath().value()
            return URI.create(context + url)
        }
        return URI.create(location)
    }

}