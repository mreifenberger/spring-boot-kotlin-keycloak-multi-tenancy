package com.sticonsulting.servicea.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class TenantResolver : HandlerInterceptor {

    @Autowired
    private lateinit var requestScopedBean: RequestScopedBean

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val realm = TenantContext.getCurrentTenant()
        requestScopedBean.setRealm(realm)
        return true
    }
}

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class RequestScopedBean {
    private var realm: String? = null

    fun setRealm(realm: String) {
        this.realm = realm
    }

    fun getRealm(): String? {
        return this.realm
    }
}
