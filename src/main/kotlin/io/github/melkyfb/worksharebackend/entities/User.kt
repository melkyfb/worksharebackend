package io.github.melkyfb.worksharebackend.entities

import io.github.melkyfb.worksharebackend.configs.WSPasswordEncoder
import javax.persistence.*

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var surname: String,
    var firstName: String,
    var username: String,
    var password: String,

    @ManyToMany(
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "role_user",
        joinColumns = arrayOf(
            JoinColumn(name = "role_id", referencedColumnName = "id")
        ),
        inverseJoinColumns = arrayOf(
            JoinColumn(name = "user_id", referencedColumnName = "id")
        )
    )
    var roles: List<Role>?
)