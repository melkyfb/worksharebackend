package io.github.melkyfb.worksharebackend.configs

import io.github.melkyfb.worksharebackend.entities.UserDetails
import io.github.melkyfb.worksharebackend.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class WSUserDetailsService(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): User {
        if (username.isNullOrEmpty()) {
            throw UsernameNotFoundException("missing username")
        }
        var user = userRepository.findByUsername(username)
        if (user == null) {
            throw UsernameNotFoundException(user)
        }
        var roles = HashSet<GrantedAuthority>()
        for (role in user.roles!!) {
            roles.add(SimpleGrantedAuthority(role.role))
        }
        return User(user.username, user.password, roles)
    }

}