package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*

class FourKingsFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.KING)) {
        MeldCombination(MeldCombinationType.FOUR_KINGS, 80)
    }


}