package io.github.melkyfb.worksharebackend.repositories

import com.google.gson.Gson
import io.github.melkyfb.worksharebackend.entities.Role
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
    fun shouldContextLoad() {
        assertThat(roleRepository).isNotNull()
        assertThat(mvc).isNotNull
    }

    @Test
    @WithUserDetails("admin")
    fun shouldCreateRole() {
        var role = Role(
            null,
            "roleTest1",
            "descriptionTest1",
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
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.role").value("roleTest1"))
            .andExpect(jsonPath("$.description").value("descriptionTest1"))
    }

    @Test
    @WithUserDetails("admin")
    fun shouldFailToCreateWhenRoleExists() {
        var role = Role(
            null,
            "roleTest2",
            "descriptionTest2",
            null
        )
        assertThat(roleRepository.save(role))
            .isNotNull()
        var roleDup = Role(
            null,
            "roleTest2",
            "descriptionTest2",
            null
        )
        var gson = Gson()
        var roleJson = gson.toJson(roleDup)

        mvc.perform(
            MockMvcRequestBuilders
                .post("/roles")
                .content(roleJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isConflict)
    }

    @Test
    @WithUserDetails("admin")
    fun shouldUpdateRole() {
        var roleCreate = Role(
            null,
            "roleTest3",
            "descriptionTest3",
            null
        )

        var role = roleRepository.save(roleCreate)

        assertThat(role).isNotNull

        role!!.description = "updatedDescriptionTest3"

        var gson = Gson()
        var roleJson = gson.toJson(role)

        mvc.perform(
            MockMvcRequestBuilders
                .post("/roles")
                .content(roleJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.role").value("roleTest3"))
            .andExpect(jsonPath("$.description").value("updatedDescriptionTest3"))
    }

    @Test
    @WithUserDetails("admin")
    fun shouldDeleteRole() {
        var role = Role(
            null,
            "roleTest4",
            "descriptionTest4",
            null
        )
        assertThat(roleRepository.save(role)).isNotNull()

        var roleDB = roleRepository.findByRole("roleTest4")
        assertThat(roleDB).isNotNull()

        mvc.perform(
            MockMvcRequestBuilders
                .delete("/roles/${roleDB!!.id}")
        )
            .andExpect(status().isNoContent)

        var roleDBAfterDelete = roleRepository.findByRole("roleTest4")
        assertThat(roleDBAfterDelete).isNull()
    }

}