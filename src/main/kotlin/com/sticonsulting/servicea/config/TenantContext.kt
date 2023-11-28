package com.sticonsulting.servicea.config

object TenantContext {
    private val currentTenant: ThreadLocal<String> = ThreadLocal()

    fun setCurrentTenant(tenantId: String) {
        currentTenant.set(tenantId)
    }

    fun getCurrentTenant(): String {
        return currentTenant.get() ?: "defaultRealm" // Default if none set
    }

    fun clear() {
        currentTenant.remove()
    }
}
