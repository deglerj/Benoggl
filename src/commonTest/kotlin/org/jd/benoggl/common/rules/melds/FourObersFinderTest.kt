package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FourObersFinderTest {

    val sut = FourObersFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertEquals(MeldCombinationType.FOUR_OBERS, combinations.first().type)
        assertEquals(60, combinations.first().points)
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertEquals(2, combinations.size)
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

}