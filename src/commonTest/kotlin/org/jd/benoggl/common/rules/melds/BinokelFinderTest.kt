package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class BinokelFinderTest {

    val sut = BinokelFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertEquals(MeldCombinationType.BINOKEL, combinations.first().type)
        assertEquals(40, combinations.first().points)
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

}