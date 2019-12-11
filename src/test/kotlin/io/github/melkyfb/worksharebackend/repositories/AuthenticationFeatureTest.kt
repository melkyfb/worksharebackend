package io.github.melkyfb.worksharebackend.repositories

import com.google.gson.Gson
import io.github.melkyfb.worksharebackend.entities.Role
import io.github.melkyfb.worksharebackend.entities.User
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.lang.reflect.Modifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaGetter


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationFeatureTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun contextLoad() {
        assertThat(mvc).isNotNull
    }

    fun loadResourcesFail(resource: String) {
        mvc.perform(
            MockMvcRequestBuilders
                .post("/${resource}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}")
        )
            .andExpect(status().isUnauthorized)

        mvc.perform(
            MockMvcRequestBuilders
                .get("/${resource}")
        )
            .andExpect(status().isUnauthorized)

        mvc.perform(
            MockMvcRequestBuilders
                .get("/${resource}/1")
        )
            .andExpect(status().isUnauthorized)

        mvc.perform(
            MockMvcRequestBuilders
                .delete("/${resource}/1")
        )
            .andExpect(status().isUnauthorized)
    }

    fun loadResourcesSuccess(resource: String, dataClass: Object) {
        var gson = Gson()
        var json = gson.toJson(dataClass)
        var createResponse = mvc.perform(
            MockMvcRequestBuilders
                .post("/${resource}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn()
        assertThat(createResponse).isNotNull

        var jsonResponse = JSONObject(createResponse.response.contentAsString)
        assertThat(jsonResponse).isNotNull
        var id = jsonResponse.get("id")
        assertThat(id).isNotNull
        assertThat(id as Int).isGreaterThan(0)

        mvc.perform(
            MockMvcRequestBuilders
                .get("/$resource")
        )
            .andExpect(status().isOk)

        mvc.perform(
            MockMvcRequestBuilders
                .get("/$resource/$id")
        )
            .andExpect(status().isOk)

        mvc.perform(
            MockMvcRequestBuilders
                .delete("/$resource/$id")
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun shouldUserResourcesFailIfNotCreds() {
        loadResourcesFail("users")
    }

    @Test
    fun shouldRoleResourcesFailIfNotCreds() {
        loadResourcesFail("roles")
    }

    @Test
    @WithUserDetails("admin")
    fun shouldUserResourcesSuccessWithCreds() {
        var u = User(
            null,
            "testUser",
            "testUser",
            "testUser",
            "testUser",
            null
        )
        loadResourcesSuccess("users", u as Object)
    }

    @Test
    @WithUserDetails("admin")
    fun shouldRoleResourcesSuccessWithCreds() {
        var r = Role(
            null,
            "testRole",
            "testRole",
            null
        )
        loadResourcesSuccess("roles", r as Object)
    }

}