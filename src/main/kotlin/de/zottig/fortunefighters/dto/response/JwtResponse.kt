package de.zottig.fortunefighters.dto.response

class JwtResponse(var accessToken: String, var id: Long, var username: String, var email: String, val roles: List<String>) {
    var tokenType = "Bearer"

}