package io.github.melkyfb.worksharebackend.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WSWebSecurityConfigurerAdapter(
    val wsUserDetailsService: WSUserDetailsService
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder() = WSPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!
            .userDetailsService(wsUserDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity?) {
        http!!.httpBasic()

            .and()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/users/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/roles/**").hasAnyRole("USER", "ADMIN")

            .and()
            .cors()

            .and()
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().sameOrigin()
    }
}