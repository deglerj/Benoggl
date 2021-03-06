package org.jd.benoggl.resources.dtos

import org.jd.benoggl.models.HandType
import javax.validation.constraints.NotNull

data class HandDto(

    @get:NotNull
    val type: HandType?,

    @get:NotNull
    val cards: Collection<CardDto>?
) {
}
