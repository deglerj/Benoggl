package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.jd.benoggl.models.findRanks

class ProcessionFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val kings = findSuits(Rank.KING, cards)
        val distinctKings = kings.toSet()
        val obers = findSuits(Rank.KING, cards)
        val distinctObers = obers.toSet()

        val combinations = when {
            kings.size == 8 && obers.size == 8 -> 2
            distinctKings.size == 4 && distinctObers.size == 4 -> 1
            else -> 0
        }
        return List(combinations) {
            MeldCombination(
                type = MeldCombinationType.PROCESSION,
                points = 240,
                blockedCombinations = blockPairs(trump)
            )
        }
    }

    private fun findSuits(rank: Rank, cards: Collection<Card>) = cards
        .findRanks(rank)
        .map(Card::suit)

    private fun blockPairs(trump: Suit): Collection<BlockedMeldCombination> {
        val nonTrumpSuits = Suit.values().filterNot { it == trump }

        val blocked = nonTrumpSuits
            .map { suit -> BlockedMeldCombination(MeldCombinationType.PAIR, suit) }
            .toMutableList()
        blocked.add(BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, trump))
        return blocked
    }
}