package de.zottig.fortunefighters.dto.request

import javax.validation.constraints.NotEmpty

class LoginRequest {
    @NotEmpty
    var username: String? = null

    @NotEmpty
    var password: String? = null
}