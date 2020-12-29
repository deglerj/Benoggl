package org.jd.benoggl.rest.dtos

import org.jd.benoggl.model.HandType
import javax.validation.constraints.NotNull

data class HandDto(

    @get:NotNull
    val type: HandType?,

    @get:NotNull
    val cards: Collection<CardDto>?
) {
}
