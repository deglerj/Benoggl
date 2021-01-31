package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.*

class DoubleBinokelFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val leafObers = cards.findSuits(Suit.LEAVES).findRanks(Rank.OBER).size
        val bellsUnters = cards.findSuits(Suit.BELLS).findRanks(Rank.UNTER).size

        if (leafObers != 2 || bellsUnters != 2) {
            return emptyList()
        }

        return listOf(
            MeldCombination(MeldCombinationType.DOUBLE_BINOKEL, 300)
        )
    }
}