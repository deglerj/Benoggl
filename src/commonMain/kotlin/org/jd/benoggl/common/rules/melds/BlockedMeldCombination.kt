package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.MeldCombination
import org.jd.benoggl.common.models.Suit

data class BlockedMeldCombination(val type: MeldCombinationType, val suit: Suit? = null) {

    fun isBlocking(combination: MeldCombination) =
        this.type == combination.type && (this.suit == null || this.suit == combination.suit)

}