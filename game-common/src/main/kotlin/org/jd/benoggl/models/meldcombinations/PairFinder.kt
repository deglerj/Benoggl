package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.*
import org.jd.benoggl.removeFirst

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
            .map { suit -> MeldCombination("Paar (${suit.name})", 20) }
    }

}