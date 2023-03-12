package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FourAcesFinderTest {

    val sut = FourAcesFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE),
                Card(Suit.LEAVES, Rank.ACE)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 1)
        assertEquals(combinations.first().type, MeldCombinationType.FOUR_ACES)
        assertEquals(combinations.first().points, 100)
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE),
                Card(Suit.LEAVES, Rank.ACE),
                Card(Suit.LEAVES, Rank.ACE)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 2)
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

}