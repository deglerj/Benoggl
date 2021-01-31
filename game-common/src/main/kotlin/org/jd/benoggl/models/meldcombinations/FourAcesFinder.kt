package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.jd.benoggl.models.countFourOfAKind

class FourAcesFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.ACE)) {
        MeldCombination(MeldCombinationType.FOUR_ACES, 100)
    }


}