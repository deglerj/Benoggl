package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*

class FourObersFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.OBER)) {
        MeldCombination(MeldCombinationType.FOUR_OBERS, 60)
    }

}