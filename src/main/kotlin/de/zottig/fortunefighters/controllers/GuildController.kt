package de.zottig.fortunefighters.controllers

import de.zottig.fortunefighters.dto.GuildDto
import de.zottig.fortunefighters.models.Guild
import de.zottig.fortunefighters.repositories.GuildRepository
import de.zottig.fortunefighters.security.services.UserDetailsImpl
import lombok.extern.slf4j.Slf4j
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api")
@Slf4j
class GuildController {

    @Autowired
    var modelMapper: ModelMapper? = null

    @Autowired
    var guildRepository: GuildRepository? = null

    @GetMapping("/guild")
    fun getGuild(): GuildDto? {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as UserDetailsImpl
        println(userDetails.id)
        val guild = userDetails.id?.let { guildRepository?.findByUserId(it) }
        println(guild)
        println(guild?.name)
        return guild?.let { convertToDto(it) }
    }

    private fun convertToDto(guild: Guild): GuildDto {
        val postDto: GuildDto = modelMapper!!.map(guild, GuildDto::class.java)
        return postDto
    }
}