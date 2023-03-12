package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*

class FourUntersFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.UNTER)) {
        MeldCombination(MeldCombinationType.FOUR_UNTERS, 40)
    }


}