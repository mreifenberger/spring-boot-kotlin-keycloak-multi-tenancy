package com.sticonsulting.servicea.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TenantIdentificationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val path = request.requestURI
        val parts = path.split("/")
        val realm = if (parts.size > 1) parts[1] else "defaultRealm" // Extracting realm from the path

        logger.info("Received realm: $realm")

        TenantContext.setCurrentTenant(realm)
        try {
            filterChain.doFilter(request, response)
        } finally {
            TenantContext.clear()
        }
    }
}
