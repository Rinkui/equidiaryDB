package equidiaryDB.security

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import equidiaryDB.EquidiaryDB.equiLogger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

data class JWToken @JsonCreator constructor(@JsonProperty("jwt") @get:JsonProperty val jwt: String)

class TokenProvider {
    private val key: Key
    private val jwtParser: JwtParser

    init {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_BASE64))
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build()
    }

    fun createToken(userName: String): JWToken {
        val validity = Instant.now().plus(tokenValidityInMinutes, ChronoUnit.MINUTES)
        return JWToken(
            Jwts
                .builder()
                .setSubject(userName)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(Date.from(validity))
                .compact()
        )
    }

    fun renewToken(token: String): JWToken {
        val body = jwtParser.parseClaimsJws(token).body
        val validity = Instant.now().plus(tokenValidityInMinutes, ChronoUnit.MINUTES)
        return JWToken(
            Jwts
                .builder()
                .setSubject(body.subject)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(Date.from(validity))
                .compact()
        )
    }

    fun isTokenValid(authToken: String?): Boolean {
        if (authToken.isNullOrBlank()) {
            return false
        }

        try {
            jwtParser.parseClaimsJws(authToken)
            return true
        } catch (e: ExpiredJwtException) {
            equiLogger.info("$INVALID_JWT_TOKEN: ${e.message}")
        } catch (e: Exception) {
            equiLogger.error(INVALID_JWT_TOKEN, e)
        }
        return false
    }

    companion object {
        private const val tokenValidityInMinutes: Long = 6
        private const val INVALID_JWT_TOKEN = "Invalid JWT token"
        private const val SECRET_BASE64 =
            "b2p1bzJWN2RHNUkwNjZzOHVGTDMxdFBtWWhqN1gzSUNmQTFlbXU1dVB1TUxDdXVZcFJLV1JjM2h4RUxvbWFGYkt2eGViRVZLWTdnSmtGTGVQdWZwYlhxTmNjek56VDVpQll3UUFabmNuaUZjcnFBcVBrcVNxa3NCTTh6a0w1WENvQWJ6T21LcHNUNXByN2dlUlZJVlUxSjdrRWFoZDJJZUtwVm1tMkxXMG8="
    }
}
