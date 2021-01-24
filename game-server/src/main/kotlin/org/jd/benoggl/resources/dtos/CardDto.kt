package org.jd.benoggl.resources.dtos

import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import javax.validation.constraints.NotNull

data class CardDto(

    @get:NotNull
    val suit: Suit?,

    @get:NotNull
    val rank: Rank?
) {
}
