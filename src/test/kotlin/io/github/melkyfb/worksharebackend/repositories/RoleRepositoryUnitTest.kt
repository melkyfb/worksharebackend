package io.github.melkyfb.worksharebackend.repositories

import io.github.melkyfb.worksharebackend.entities.Role
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class RoleRepositoryUnitTest {

    @Autowired
    lateinit var roleRepository: RoleRepository

    var role = Role(
        null,
        "ROLE_TEST_ADMIN",
        null,
        null
    )

    @Test
    fun findByRole_thenReturnRole() {
        role = roleRepository.save(role)
        var roleDB = roleRepository.findByRole(role.role)
        assertThat(roleDB).isNotNull()
        assertThat(roleDB!!).isEqualTo(role)
    }

    @Test
    fun findByRole_thenDelete() {
        role = roleRepository.save(role)
        var roleDB = roleRepository.findByRole("ROLE_TEST_ADMIN")
        assertThat(roleDB).isNotNull()

        assertThat(roleDB!!.id).isGreaterThan(0)

        assertThat(roleRepository.existsById(roleDB!!.id!!))

        roleRepository.delete(roleDB!!)

        roleDB = roleRepository.findByRole("ROLE_TEST_ADMIN")
        assertThat(roleDB).isNull()
    }
}