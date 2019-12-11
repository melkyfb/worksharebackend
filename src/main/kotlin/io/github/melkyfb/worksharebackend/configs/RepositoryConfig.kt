package io.github.melkyfb.worksharebackend.configs

import io.github.melkyfb.worksharebackend.entities.Role
import io.github.melkyfb.worksharebackend.entities.User
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter


@Configuration
class RepositoryConfig : RepositoryRestConfigurerAdapter() {
    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        config.exposeIdsFor(Role::class.java)
        config.exposeIdsFor(User::class.java)
    }
}