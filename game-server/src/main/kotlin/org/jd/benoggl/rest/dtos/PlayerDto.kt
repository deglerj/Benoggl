package org.jd.benoggl.rest.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PlayerDto(

    @get:NotNull
    @get:NotBlank
    val uid: String?,

    @get:NotNull
    @get:NotBlank
    val name: String?
) {
}
