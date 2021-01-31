package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.jd.benoggl.models.countFourOfAKind

class FourObersFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit) = List(cards.countFourOfAKind(Rank.OBER)) {
        MeldCombination(MeldCombinationType.FOUR_OBERS, 60)
    }


}