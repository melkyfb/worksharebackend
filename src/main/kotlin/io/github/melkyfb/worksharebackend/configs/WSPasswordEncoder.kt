package io.github.melkyfb.worksharebackend.configs

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class WSPasswordEncoder : PasswordEncoder {

    fun passwordEncoder() = BCryptPasswordEncoder()

    override fun encode(password: CharSequence?): String {
        return passwordEncoder().encode(password)
    }

    override fun matches(candidate: CharSequence?, hash: String?): Boolean {
        return passwordEncoder().matches(candidate, hash)
    }
}