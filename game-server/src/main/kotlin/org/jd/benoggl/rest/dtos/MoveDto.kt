package org.jd.benoggl.rest.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MoveDto(

    @get:NotNull
    @get:NotBlank
    val playerUid: String?,

    @get:NotNull
    val card: CardDto?
) {
}
