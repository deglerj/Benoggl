package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.Suit

data class MeldCombination(
    val type: MeldCombinationType,
    val points: Int,
    val suit: Suit? = null,
    val blockedCombinations: Collection<BlockedMeldCombination> = emptyList()
)