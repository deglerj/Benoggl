package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.*

class FourKingsFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.KING)) {
        MeldCombination(MeldCombinationType.FOUR_KINGS, 80)
    }


}