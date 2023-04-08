package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TrumpFamilyFinderTest {

    val sut = TrumpFamilyFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneFamily() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertEquals(MeldCombinationType.TRUMP_FAMILY, combinations.first().type)
        assertEquals(150, combinations.first().points)
        assertContains(
            combinations.first().blockedCombinations,
            BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, Suit.ACORNS)
        )
    }

    @Test
    fun twoFamilies() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + createFamily(Suit.BELLS),
            Suit.BELLS
        )

        assertEquals(2, combinations.size)
        combinations.forEach { combination ->
            assertContains(
                combination.blockedCombinations,
                BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, Suit.BELLS)
            )
        }
    }

    @Test
    fun oneFamilyAndOneIncompleteFamilyOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS)
                    + listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.ACORNS, Rank.TEN),
                Card(Suit.ACORNS, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertContains(
            combinations.first().blockedCombinations,
            BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, Suit.ACORNS)
        )
    }

    @Test
    fun noPairs() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.ACORNS, Rank.ACE)
            ),
            Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

    private fun createFamily(suit: Suit): Collection<Card> =
        listOf(
            Card(suit, Rank.ACE),
            Card(suit, Rank.TEN),
            Card(suit, Rank.KING),
            Card(suit, Rank.OBER),
            Card(suit, Rank.UNTER)
        )


}