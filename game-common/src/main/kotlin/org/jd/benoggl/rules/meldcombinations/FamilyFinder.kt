package org.jd.benoggl.rules.meldcombinations

import org.jd.benoggl.models.*
import org.jd.benoggl.removeFirst

class FamilyFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val remainingCards = cards.toMutableList()

        val suitFamilies = cards.toMutableList()
            .withoutSuits(trump)
            .filter { findAndRemoveFamily(it.suit, remainingCards) }
            .map { it.suit }

        return suitFamilies
            .map { suit -> MeldCombination(MeldCombinationType.FAMILY, 100, suit, blockPair(suit)) }
    }

    private fun findAndRemoveFamily(suit: Suit, cards: MutableList<Card>): Boolean =
        // @formatter:off
        cards.removeFirst { it.suit == suit && it.rank == Rank.ACE } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.TEN } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.KING } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.OBER } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.UNTER } != null
        // @formatter:on

    private fun blockPair(suit: Suit): Collection<BlockedMeldCombination> =
        listOf(BlockedMeldCombination(MeldCombinationType.PAIR, suit))
}