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

        assertEquals(1, combinations.size)
        assertEquals(MeldCombinationType.FOUR_ACES, combinations.first().type)
        assertEquals(100, combinations.first().points)
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

        assertEquals(2, combinations.size)
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