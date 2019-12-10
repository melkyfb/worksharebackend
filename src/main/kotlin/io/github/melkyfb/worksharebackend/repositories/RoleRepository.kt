package io.github.melkyfb.worksharebackend.repositories

import io.github.melkyfb.worksharebackend.entities.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface RoleRepository : CrudRepository<Role, Long> {
    fun findByRole(role: String): Role?
}