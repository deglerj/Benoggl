package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.*

class FourUntersFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.UNTER)) {
        MeldCombination(MeldCombinationType.FOUR_UNTERS, 40)
    }


}