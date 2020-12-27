package org.jd.benoggl.rest

import org.jd.benoggl.model.Rank
import org.jd.benoggl.model.Suit
import javax.validation.constraints.NotNull

class CardDto() {

    @NotNull
    lateinit var suit: Suit

    @NotNull
    lateinit var rank: Rank
}
