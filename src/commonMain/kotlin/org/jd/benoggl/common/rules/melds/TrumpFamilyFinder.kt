package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*

class TrumpFamilyFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val trumpCards = cards.findSuits(trump)

        val familiesCount = listOf(
            trumpCards.findRanks(Rank.ACE).size,
            trumpCards.findRanks(Rank.TEN).size,
            trumpCards.findRanks(Rank.KING).size,
            trumpCards.findRanks(Rank.OBER).size,
            trumpCards.findRanks(Rank.UNTER).size
        ).min()

        return List(familiesCount) {
            MeldCombination(MeldCombinationType.TRUMP_FAMILY, 150, trump, blockPair(trump))
        }
    }

    private fun blockPair(trump: Suit): Collection<BlockedMeldCombination> =
        listOf(BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, trump))
}