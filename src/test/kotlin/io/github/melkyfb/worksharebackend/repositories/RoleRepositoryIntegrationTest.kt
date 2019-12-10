package io.github.melkyfb.worksharebackend.repositories

import com.google.gson.Gson
import io.github.melkyfb.worksharebackend.entities.Role
import io.github.melkyfb.worksharebackend.entities.User
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class RoleRepositoryIntegrationTest {

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun contextLoadedFine() {
        assertThat(roleRepository).isNotNull()
        assertThat(mvc).isNotNull
    }

    @Test
    fun unauthorizedWhenNoCreds() {
        mvc.perform(MockMvcRequestBuilders.get("/roles"))
            .andDo(print())
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithUserDetails("admin")
    fun authorizedWhenCreds() {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/roles").accept(MediaType.APPLICATION_JSON)

        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    }

    @Test
    @WithUserDetails("admin")
    fun createUser() {
        var role = Role(
            null,
            "roleTest",
            "descriptionTest",
            null
        )
        var gson = Gson()
        var roleJson = gson.toJson(role)

        mvc.perform(
            MockMvcRequestBuilders
                .post("/roles")
                .content(roleJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.role").value("roleTest"))
            .andExpect(jsonPath("$.description").value("descriptionTest"))
    }

}