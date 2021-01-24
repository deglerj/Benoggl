package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.*

class TrumpFamilyFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val trumpCards = cards.findSuits(trump)

        val familiesCount = listOf(
            trumpCards.findRanks(Rank.ACE).size,
            trumpCards.findRanks(Rank.TEN).size,
            trumpCards.findRanks(Rank.KING).size,
            trumpCards.findRanks(Rank.OBER).size,
            trumpCards.findRanks(Rank.UNTER).size
        ).min() ?: 0

        return List(familiesCount) {
            MeldCombination("Trumpf-Familie (${trump.name})", 150)
        }
    }
}