package com.sachet.apigatewayamazon.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class JwtService {

    @Value("\${secure_key}")
    val SECURE_KEY: String ?= null

    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun extractUserName(token: String): String {
        val claims = extractAllClaims(token)
        return claims.subject
    }

    fun extractExpireDate(token: String): Date {
        val claims = extractAllClaims(token)
        return claims.expiration
    }

    fun extractAllClaims(token: String): Claims {
        val key = Keys.hmacShaKeyFor(SECURE_KEY?.toByteArray())
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(userName: String, ex: Date): Boolean {
        val matcher = EMAIL_ADDRESS_PATTERN.matcher(userName)
        return ex.after(Date()) && matcher.matches()
    }

}