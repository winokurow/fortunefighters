package de.zottig.fortunefighters.payloads.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

class SignupRequest {
    @NotEmpty
    @Size(min = 3, max = 20)
    var username: String? = null

    @NotEmpty
    @Size(max = 50)
    @Email
    var email: String? = null
    var role: Set<String>? = null

    @NotEmpty
    @Size(min = 6, max = 40)
    var password: String? = null
}