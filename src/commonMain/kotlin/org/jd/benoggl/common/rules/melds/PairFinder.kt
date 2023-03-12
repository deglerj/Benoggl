package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.*
import org.jd.benoggl.common.removeFirst

class PairFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val nonTrumpSuits = Suit.values().filterNot { it == trump }.toTypedArray()

        val kings = cards.findRanks(Rank.KING).findSuits(*nonTrumpSuits)
        val obers = cards.findRanks(Rank.OBER).findSuits(*nonTrumpSuits).toMutableList()

        val pairedSuits = kings
            .filter { king ->
                obers.removeFirst { ober -> ober.suit == king.suit } != null
            }
            .map { it.suit }

        return pairedSuits
            .map { suit -> MeldCombination(MeldCombinationType.PAIR, 20, suit) }
    }

}