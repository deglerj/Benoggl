package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.MeldCombination
import org.jd.benoggl.common.models.Suit

interface MeldCombinationFinder {

    fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination>

}