package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class PairFinderTest {

    val sut = PairFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun onePair() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 1)
        assertEquals(combinations.first().type, MeldCombinationType.PAIR)
        assertEquals(combinations.first().points, 20)
        assertEquals(combinations.first().suit, Suit.BELLS)
    }

    @Test
    fun onePairOfEach() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 3)
    }

    @Test
    fun twoPairsOfSameSuit() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 2)
    }

    @Test
    fun onePairAndOneIncompletePairOfSameSuit() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 1)
    }

    @Test
    fun noPairs() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.ACE)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

}