package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.*

class FourAcesFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.ACE)) {
        MeldCombination(MeldCombinationType.FOUR_ACES, 100)
    }


}