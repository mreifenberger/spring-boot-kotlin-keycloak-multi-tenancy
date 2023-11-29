package com.sticonsulting.servicea.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val realm = TenantContext.getCurrentTenant()
        val redirectUrl = "/$realm/api/hello" // Construct the URL based on the realm
        response.sendRedirect(redirectUrl)
    }
}
