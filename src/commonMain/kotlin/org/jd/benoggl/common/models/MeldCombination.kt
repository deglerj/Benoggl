package org.jd.benoggl.common.models

import org.jd.benoggl.common.rules.melds.BlockedMeldCombination
import org.jd.benoggl.common.rules.melds.MeldCombinationType

data class MeldCombination(
    val type: MeldCombinationType,
    val points: Int,
    val suit: Suit? = null,
    val blockedCombinations: Collection<BlockedMeldCombination> = emptyList()
)