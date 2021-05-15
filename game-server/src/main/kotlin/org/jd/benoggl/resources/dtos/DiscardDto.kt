package org.jd.benoggl.resources.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class DiscardDto(

    @get:NotNull
    @get:NotEmpty
    val cards: Collection<CardDto>?,

    @get:NotNull
    @get:NotBlank
    val playerUid: String?
) {
}
