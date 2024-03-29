package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*

class FourAcesFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.ACE)) {
        MeldCombination(MeldCombinationType.FOUR_ACES, 100)
    }

}