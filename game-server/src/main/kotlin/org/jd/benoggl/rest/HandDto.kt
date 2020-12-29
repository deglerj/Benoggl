package org.jd.benoggl.rest

import org.jd.benoggl.model.HandType
import javax.validation.constraints.NotNull

class HandDto() {

    @NotNull
    lateinit var type: HandType

    @NotNull
    lateinit var cards: MutableCollection<CardDto>
}
