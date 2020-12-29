package org.jd.benoggl.rest.dtos

import org.jd.benoggl.model.Rank
import org.jd.benoggl.model.Suit
import javax.validation.constraints.NotNull

data class CardDto(

    @get:NotNull
    val suit: Suit?,

    @get:NotNull
    val rank: Rank?
) {
}
