package de.zottig.fortunefighters.models

import lombok.Data
import java.util.HashSet
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Data
@Entity
@Table(name = "users",
        uniqueConstraints=arrayOf(
                UniqueConstraint(columnNames = arrayOf("username", "email"))))
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    var username: @Size(max = 20) String? = null

    @NotNull
    var email: @Size(max = 50) @Email String? = null

    @NotNull
    var password: @Size(max = 120) String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: MutableSet<Role> = HashSet()

    constructor() {}
    constructor(username: String?, email: String?, password: String?) {
        this.username = username
        this.email = email
        this.password = password
    }
}