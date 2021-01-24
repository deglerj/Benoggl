package org.jd.benoggl.resources.dtos

import org.jd.benoggl.models.RoundState
import org.jd.benoggl.models.RoundType
import org.jd.benoggl.models.Suit
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RoundDto(

    @get:NotNull
    @get:NotBlank
    val number: Int?,

    @get:NotNull
    val state: RoundState?,

    @get:NotNull
    val type: RoundType?,

    val trump: Suit?
) {
}
