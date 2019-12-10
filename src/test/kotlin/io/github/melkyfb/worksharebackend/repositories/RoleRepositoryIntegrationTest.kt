package io.github.melkyfb.worksharebackend.repositories

import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.Entity

@RunWith(SpringRunner::class)
@DataJpaTest
class RoleRepositoryIntegrationTest(
    val roleRepository: RoleRepository
) {
    fun findByUsername_thenReturnUser() {
        assert(value = true)
    }
}