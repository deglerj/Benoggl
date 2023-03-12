package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.assertContainsInAnyOrder
import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ProcessionFinderTest {


    val sut = ProcessionFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun noProcession() {
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

    @Test
    fun oneProcession() {
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

        assertEquals(combinations.size, 1)
        assertEquals(combinations.first().type, MeldCombinationType.PROCESSION)
        assertEquals(combinations.first().points, 240)
        assertNull(combinations.first().suit)
        assertContainsInAnyOrder(
            combinations.first().blockedCombinations, listOf(
                BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, Suit.ACORNS),
                BlockedMeldCombination(MeldCombinationType.PAIR, Suit.BELLS),
                BlockedMeldCombination(MeldCombinationType.PAIR, Suit.HEARTS),
                BlockedMeldCombination(MeldCombinationType.PAIR, Suit.LEAVES)
            )
        )
    }

    @Test
    fun twoProcessions() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        assertEquals(combinations.size, 2)
        combinations.forEach { assertEquals(it.type, MeldCombinationType.PROCESSION) }
        combinations.forEach { assertEquals(it.points, 240) }
        combinations.forEach { assertNull(it.suit) }
    }

}