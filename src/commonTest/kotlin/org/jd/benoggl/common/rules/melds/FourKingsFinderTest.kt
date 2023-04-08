package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FourKingsFinderTest {

    val sut = FourKingsFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.LEAVES, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertEquals(MeldCombinationType.FOUR_KINGS, combinations.first().type)
        assertEquals(80, combinations.first().points)
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertEquals(2, combinations.size)
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

}