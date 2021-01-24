package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.*
import org.jd.benoggl.removeFirst

class TrumpPairFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val kings = cards.findRanks(Rank.KING).findSuits(trump)
        val obers = cards.findRanks(Rank.OBER).findSuits(trump).toMutableList()

        val pairedSuits = kings
            .filter { king ->
                obers.removeFirst { ober -> ober.suit == king.suit } != null
            }
            .map { it.suit }

        return pairedSuits
            .map { suit -> MeldCombination("Trumpf-Paar (${suit.name})", 40) }
    }

}