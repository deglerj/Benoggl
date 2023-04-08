package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FamilyFinderTest {

    val sut = FamilyFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneFamily() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertEquals(MeldCombinationType.FAMILY, combinations.first().type)
        assertEquals(100, combinations.first().points)
        assertContains(
            combinations.first().blockedCombinations,
            BlockedMeldCombination(MeldCombinationType.PAIR, Suit.BELLS)
        )
    }

    @Test
    fun oneFamilyOfEach() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS)
                    + createFamily(Suit.BELLS)
                    + createFamily(Suit.HEARTS)
                    + createFamily(Suit.LEAVES),
            Suit.ACORNS
        )

        assertEquals(3, combinations.size)
        combinations.forEach { combination ->
            assertContains(
                combination.blockedCombinations,
                BlockedMeldCombination(MeldCombinationType.PAIR, combination.suit)
            )
        }
    }

    @Test
    fun twoFamiliesOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + createFamily(Suit.BELLS),
            Suit.ACORNS
        )

        assertEquals(2, combinations.size)
        combinations.forEach { combination ->
            assertContains(
                combination.blockedCombinations,
                BlockedMeldCombination(MeldCombinationType.PAIR, Suit.BELLS)
            )
        }

    }

    @Test
    fun oneFamilyAndOneIncompleteFamilyOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + listOf(
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.TEN),
                Card(Suit.BELLS, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertEquals(1, combinations.size)
        assertContains(
            combinations.first().blockedCombinations,
            BlockedMeldCombination(MeldCombinationType.PAIR, Suit.BELLS)
        )
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

    private fun createFamily(suit: Suit): Collection<Card> =
        listOf(
            Card(suit, Rank.ACE),
            Card(suit, Rank.TEN),
            Card(suit, Rank.KING),
            Card(suit, Rank.OBER),
            Card(suit, Rank.UNTER)
        )


}