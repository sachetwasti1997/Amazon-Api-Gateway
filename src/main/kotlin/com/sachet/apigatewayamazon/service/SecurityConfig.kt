package com.sachet.apigatewayamazon.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
@Configuration
class SecurityConfig(
    private val authenticationManager: AuthenticationManager,
    private val authenticationConvertor: AuthenticationConvertor
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange { auth ->
                run {
                    auth.pathMatchers("/api/v1/user/signup").permitAll()
                        .pathMatchers("/api/v1/user/login").permitAll()
                        .anyExchange().authenticated()
                }
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .authenticationManager (authenticationManager)
            .securityContextRepository(authenticationConvertor)
        return  http.build()
    }

}