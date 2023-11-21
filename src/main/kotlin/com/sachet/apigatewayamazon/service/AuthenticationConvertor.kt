package com.sachet.apigatewayamazon.service

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationConvertor(
    val authenticationManager: AuthenticationManager
): ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val bearer = "Bearer "
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith(bearer) }
            .map { it.substring(bearer.length) }
            .map { UsernamePasswordAuthenticationToken(it, it) }
            .flatMap {
                authenticationManager.authenticate(it).map { auth ->
                    SecurityContextImpl(auth)
                }
            }
    }
}