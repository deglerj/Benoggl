package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.*

class TrumpPairFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val kings = cards.findRanks(Rank.KING).findSuits(trump)
        val obers = cards.findRanks(Rank.OBER).findSuits(trump).toMutableList()

        val pairCount = minOf(kings.size, obers.size)

        return List(pairCount) { MeldCombination(MeldCombinationType.TRUMP_PAIR, 40, trump) }
    }

}