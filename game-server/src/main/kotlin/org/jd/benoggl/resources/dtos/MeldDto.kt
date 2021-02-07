package org.jd.benoggl.resources.dtos

import org.jd.benoggl.models.Suit
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MeldDto(

    @get:NotNull
    val cards: Collection<CardDto>?,

    @get:NotNull
    @get:NotBlank
    val playerUid: String?,

    @get:NotNull
    @get:Min(0)
    val points: Int?,

    val trump: Suit?
) {
}
