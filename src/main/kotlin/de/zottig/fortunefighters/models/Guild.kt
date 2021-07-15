package de.zottig.fortunefighters.models

import lombok.Data
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Data
@Entity
@Table(name = "guilds")
class Guild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @Column(unique = true)
    var name: @Size(max = 50) String? = null

    @NotNull
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user", referencedColumnName = "id")
    var user: User? = null

    constructor() {}
    constructor(name: String?, user: User?) {
        this.name = name
        this.user = user
    }

}