package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.jd.benoggl.models.withoutSuits
import org.jd.benoggl.removeFirst

class FamilyFinder : MeldCombinationFinder {

    override fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val remainingCards = cards.toMutableList()

        val suitFamilies = cards.toMutableList()
            .withoutSuits(trump)
            .filter { findAndRemoveFamily(it.suit, remainingCards) }
            .map { it.suit }

        return suitFamilies
            .map { suit -> MeldCombination("Familie (${suit.name})", 100) }
    }

    private fun findAndRemoveFamily(suit: Suit, cards: MutableList<Card>): Boolean =
        // @formatter:off
        cards.removeFirst { it.suit == suit && it.rank == Rank.ACE } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.TEN } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.KING } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.OBER } != null &&
        cards.removeFirst { it.suit == suit && it.rank == Rank.UNTER } != null
        // @formatter:on
}