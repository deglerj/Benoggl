package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FourUntersFinderTest {

    val sut = FourUntersFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 1)
        assertEquals(combinations.first().type, MeldCombinationType.FOUR_UNTERS)
        assertEquals(combinations.first().points, 40)
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 2)
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

}