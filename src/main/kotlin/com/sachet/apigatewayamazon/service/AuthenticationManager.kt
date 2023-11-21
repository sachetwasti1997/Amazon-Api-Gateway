package com.sachet.apigatewayamazon.service

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    var jwtService: JwtService
): ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.justOrEmpty(authentication)
            .flatMap { auth ->
                val token = auth.credentials.toString()
                val userName = jwtService.extractUserName(token)
                val extractExpDate = jwtService.extractExpireDate(token)
                if (jwtService.validateToken(userName, extractExpDate)) {
                    return@flatMap Mono.just(UsernamePasswordAuthenticationToken(userName, token, null))
                }
                Mono.error(Exception("Token Invalid"))
            }
    }
}