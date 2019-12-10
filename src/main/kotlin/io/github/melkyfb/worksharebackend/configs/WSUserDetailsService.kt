package io.github.melkyfb.worksharebackend.configs

import io.github.melkyfb.worksharebackend.entities.UserDetails
import io.github.melkyfb.worksharebackend.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class WSUserDetailsService(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrEmpty()) {
            throw UsernameNotFoundException("missing username")
        }
        var user = userRepository.findByUsername(username)
        if (user == null) {
            throw UsernameNotFoundException(user)
        }
        return UserDetails(user)
    }

}