package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.MeldCombination
import org.jd.benoggl.models.Suit

interface MeldCombinationFinder {

    fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination>

}