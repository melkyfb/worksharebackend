package io.github.melkyfb.worksharebackend.repositories

import com.google.gson.Gson
import io.github.melkyfb.worksharebackend.entities.Role
import io.github.melkyfb.worksharebackend.entities.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserRepositorIntegrationTest {

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun shouldContextLoad() {
        assertThat(roleRepository).isNotNull
        assertThat(userRepository).isNotNull
        assertThat(mvc).isNotNull
    }

    @Test
    @WithUserDetails("admin")
    fun shouldCreateUser() {
        var user = User(
            null,
            "test1",
            "test1",
            "test1",
            "test1",
            null
        )
        var gson = Gson()
        var userJson = gson.toJson(user)
        mvc.perform(
            MockMvcRequestBuilders
                .post("/users")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.surname").value("test1"))
            .andExpect(jsonPath("$.firstName").value("test1"))
            .andExpect(jsonPath("$.username").value("test1"))
            .andExpect(jsonPath("$.password").value("test1"))
    }

    @Test
    @WithUserDetails("admin")
    fun shouldAssociateUserWithRole() {
        var user = User(
            null,
            "test2",
            "test2",
            "test2",
            "test2",
            null
        )
        userRepository.save(user)
        var role = Role(
            null,
            "test2",
            "test2",
            null
        )
        roleRepository.save(role)
        mvc.perform(
            MockMvcRequestBuilders
                .put("/users/${user.id}/roles")
                .content("/role/${role.id}")
                .contentType("text/uri-list")
        )
            .andExpect(status().isNoContent)

        var userWithRole = userRepository.findByUsername("test2")
        assertThat(userWithRole!!.roles).isNotNull
        mvc.perform(
            MockMvcRequestBuilders
                .delete("/users/${user.id}/roles/${role.id}")
                .contentType("text/uri-list")
        )
            .andExpect(status().isNoContent)

        var userWithoutRole = userRepository.findByUsername("test2")
        assertThat(userWithoutRole!!.roles!!.size).isEqualTo(0)
    }

    @Test
    @WithUserDetails("admin")
    fun shouldUpdateUser() {
        var user = User(
            null,
            "test3",
            "test3",
            "test3",
            "test3",
            null
        )
        var userDB = userRepository.save(user)
        assertThat(userDB).isNotNull
        userDB.surname = "updatedTest3"
        userDB.firstName = "updatedTest3"
        userDB.username = "updatedTest3"
        userDB.password = "updatedTest3"
        var gson = Gson()
        var userJson = gson.toJson(userDB)
        mvc.perform(
            MockMvcRequestBuilders
                .post("/users")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.surname").value("updatedTest3"))
            .andExpect(jsonPath("$.firstName").value("updatedTest3"))
            .andExpect(jsonPath("$.username").value("updatedTest3"))
            .andExpect(jsonPath("$.password").value("updatedTest3"))
    }

    @Test
    @WithUserDetails("admin")
    fun shouldDeleteUser() {
        var user = User(
            null,
            "test3",
            "test3",
            "test3",
            "test3",
            null
        )
        assertThat(userRepository.save(user)).isNotNull
        mvc.perform(
            MockMvcRequestBuilders
                .delete("/users/${user.id}")
        )
            .andExpect(status().isNoContent)
    }
}