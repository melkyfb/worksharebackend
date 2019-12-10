package io.github.melkyfb.worksharebackend.entities

import javax.persistence.*

@Entity
data class Role(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var role: String,
    var description: String?,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    var users: List<User>?
)