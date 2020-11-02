package de.zottig.fortunefighters.controllers

import de.zottig.fortunefighters.models.ERole
import de.zottig.fortunefighters.models.Role
import de.zottig.fortunefighters.models.User
import de.zottig.fortunefighters.payloads.request.LoginRequest
import de.zottig.fortunefighters.payloads.request.SignupRequest
import de.zottig.fortunefighters.payloads.response.JwtResponse
import de.zottig.fortunefighters.payloads.response.MessageResponse
import de.zottig.fortunefighters.repositories.RoleRepository
import de.zottig.fortunefighters.repositories.UserRepository
import de.zottig.fortunefighters.security.jwt.JwtUtils
import de.zottig.fortunefighters.security.services.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    var authenticationManager: AuthenticationManager? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var roleRepository: RoleRepository? = null

    @Autowired
    var encoder: PasswordEncoder? = null

    @Autowired
    var jwtUtils: JwtUtils? = null

    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest?): ResponseEntity<*> {
        val authentication: Authentication = authenticationManager!!.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest!!.username, loginRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils!!.generateJwtToken(authentication)
        val userDetails = authentication.getPrincipal() as UserDetailsImpl
        val roles = userDetails.authorities.stream()
                .map { item: GrantedAuthority -> item.authority }
                .collect(Collectors.toList())
        return ResponseEntity.ok(JwtResponse(jwt,
                userDetails.id!!,
                userDetails.username!!,
                userDetails.email!!,
                roles))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest?): ResponseEntity<*> {
        if (userRepository!!.existsByUsername(signUpRequest!!.username!!)) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository!!.existsByEmail(signUpRequest.email!!)) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Email is already in use!"))
        }

        // Create new user's account
        val user = User(signUpRequest.username,
                signUpRequest.email,
                encoder!!.encode(signUpRequest.password))
        val strRoles = signUpRequest.role
        val roles: MutableSet<Role> = HashSet<Role>()
        if (strRoles == null) {
            val userRole: Role = roleRepository!!.findByName(ERole.ROLE_USER)
                    ?: throw RuntimeException("Error: Role is not found.")
            roles.add(userRole)
        } else {
            strRoles.forEach(Consumer { role: String? ->
                when (role) {
                    "admin" -> {
                        val adminRole: Role = roleRepository!!.findByName(ERole.ROLE_ADMIN)
                                ?: throw RuntimeException("Error: Role is not found.")
                        roles.add(adminRole)
                    }
                    "mod" -> {
                        val modRole: Role = roleRepository!!.findByName(ERole.ROLE_MODERATOR)
                                ?: throw RuntimeException("Error: Role is not found.")
                        roles.add(modRole)
                    }
                    else -> {
                        val userRole: Role = roleRepository!!.findByName(ERole.ROLE_USER)
                                ?: throw RuntimeException("Error: Role is not found.")
                        roles.add(userRole)
                    }
                }
            })
        }
        user.roles=roles
        userRepository!!.save<User>(user)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }
}