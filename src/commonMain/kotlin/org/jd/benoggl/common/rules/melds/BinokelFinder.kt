package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*


class BinokelFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val leafObers = cards.findSuits(Suit.LEAVES).findRanks(Rank.OBER).size
        val bellsUnters = cards.findSuits(Suit.BELLS).findRanks(Rank.UNTER).size

        if (leafObers != 1 || bellsUnters != 1) {
            return emptyList()
        }

        return listOf(
            MeldCombination(MeldCombinationType.BINOKEL, 40)
        )
    }
}