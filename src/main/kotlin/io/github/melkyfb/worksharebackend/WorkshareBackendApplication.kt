package io.github.melkyfb.worksharebackend

import io.github.melkyfb.worksharebackend.configs.WSPasswordEncoder
import io.github.melkyfb.worksharebackend.entities.Role
import io.github.melkyfb.worksharebackend.entities.User
import io.github.melkyfb.worksharebackend.repositories.RoleRepository
import io.github.melkyfb.worksharebackend.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class WorkshareBackendApplication(
    var userRepository: UserRepository,
    var roleRepository: RoleRepository
) {

    fun passwordEncoder() = WSPasswordEncoder()

    @Bean
    fun initDatabase() = CommandLineRunner {
        val adminRole = includeRoleIfNotExists("ROLE_ADMIN", "Administrator Role")
        val userRole = includeRoleIfNotExists("ROLE_USER", "User Role")
        includeUserIfNotExists(
            User(
                null,
                "Admin",
                "Admin",
                "admin",
                passwordEncoder().encode("admin"),
                null
            ),
            arrayListOf(adminRole, userRole)
        )
    }

    fun includeUserIfNotExists(user: User, roles: ArrayList<Role>): User{
        var userDB = userRepository.findByUsername(user.username)
        if(userDB == null){
            userDB = userRepository.save(user)
        }
        userDB.roles = roles
        return userRepository.save(userDB)
    }

    fun includeRoleIfNotExists(role: String, description: String): Role{
        var roleDB = roleRepository.findByRole(role)
        if(roleDB == null){
            roleDB = roleRepository.save(Role(null, role, description, null))
        }
        return roleDB
    }
}

fun main(args: Array<String>) {
    runApplication<WorkshareBackendApplication>(*args)
}