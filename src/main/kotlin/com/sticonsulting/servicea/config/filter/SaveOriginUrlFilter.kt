package com.sticonsulting.servicea.config.filter

import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class SaveOriginUrlFilter() : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        return exchange.session.map {
            val url = exchange.request.queryParams.getFirst("originUrl")
            if (url != null)
                it.attributes["SPRING_SECURITY_SAVED_REQUEST"] = url
        }.flatMap {
            chain.filter(exchange)
        }
    }
}
