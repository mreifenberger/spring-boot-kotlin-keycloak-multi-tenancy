package com.sticonsulting.servicea.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint(private val keycloakBaseUrl: String) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val realm = TenantContext.getCurrentTenant()
        val clientId = "backend-client"

        val redirectUri = request.requestURL.toString() // Or any other URI you want to redirect back to after login
        val keycloakLoginUrl =
            "$keycloakBaseUrl/realms/$realm/protocol/openid-connect/auth?client_id=$clientId&response_type=code&redirect_uri=http://localhost:8081/testrealm/api/hello"

        response.sendRedirect(keycloakLoginUrl)
    }
}