package org.jd.benoggl.models

import org.jd.benoggl.rules.meldcombinations.BlockedMeldCombination
import org.jd.benoggl.rules.meldcombinations.MeldCombinationType

data class MeldCombination(
    val type: MeldCombinationType,
    val points: Int,
    val suit: Suit? = null,
    val blockedCombinations: Collection<BlockedMeldCombination> = emptyList()
)