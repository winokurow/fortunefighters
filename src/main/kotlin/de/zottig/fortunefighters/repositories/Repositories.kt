package de.zottig.fortunefighters.repositories

import de.zottig.fortunefighters.models.ERole
import de.zottig.fortunefighters.models.Role
import de.zottig.fortunefighters.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: ERole): Role?
}