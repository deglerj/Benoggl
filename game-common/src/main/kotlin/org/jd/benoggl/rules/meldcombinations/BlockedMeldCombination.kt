package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.MeldCombination
import org.jd.benoggl.models.Suit

data class BlockedMeldCombination(val type: MeldCombinationType, val suit: Suit?) {

    fun isBlocking(combination: MeldCombination) =
        this.type == combination.type && (this.suit == null || this.suit == combination.suit)

}