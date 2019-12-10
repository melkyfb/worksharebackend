package io.github.melkyfb.worksharebackend.repositories

import io.github.melkyfb.worksharebackend.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@RepositoryRestResource
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByUsernameAndPassword(username: String, password: String): User?
}