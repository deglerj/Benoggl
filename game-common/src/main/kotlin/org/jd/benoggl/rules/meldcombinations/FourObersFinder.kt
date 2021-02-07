package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.*

class FourObersFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.OBER)) {
        MeldCombination(MeldCombinationType.FOUR_OBERS, 60)
    }


}