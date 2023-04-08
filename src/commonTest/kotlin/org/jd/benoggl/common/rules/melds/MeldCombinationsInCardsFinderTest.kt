package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.assertContainsInAnyOrder
import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.MeldCombination
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertTrue

internal class MeldCombinationsInCardsFinderTest {

    val sut = MeldCombinationsInCardsFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun noCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        assertTrue(combinations.isEmpty())
    }

    @Test
    fun singleCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        assertContainsMeldCombinations(
            listOf(AssertedCombination(MeldCombinationType.PAIR, Suit.BELLS)),
            combinations
        )
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)

            ), Suit.ACORNS
        )

        assertContainsMeldCombinations(
            listOf(
                AssertedCombination(MeldCombinationType.TRUMP_PAIR, Suit.ACORNS),
                AssertedCombination(MeldCombinationType.PAIR, Suit.BELLS)
            ),
            combinations
        )
    }

    @Test
    fun twoIdenticalCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)

            ), Suit.ACORNS
        )

        assertContainsMeldCombinations(
            listOf(
                AssertedCombination(MeldCombinationType.PAIR, Suit.BELLS),
                AssertedCombination(MeldCombinationType.PAIR, Suit.BELLS)
            ),
            combinations
        )
    }

    @Test
    fun combinationBlocksOtherCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.TEN),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER)
            ), Suit.ACORNS
        )

        assertContainsMeldCombinations(
            listOf(
                AssertedCombination(MeldCombinationType.FAMILY, Suit.BELLS)
            ),
            combinations
        )
    }

    @Test
    fun combinationDoesntBlockCombinationWithDifferentSuit() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.TEN),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER)
            ), Suit.ACORNS
        )

        assertContainsMeldCombinations(
            listOf(
                AssertedCombination(MeldCombinationType.FAMILY, Suit.BELLS),
                AssertedCombination(MeldCombinationType.TRUMP_PAIR, Suit.ACORNS)
            ),
            combinations
        )
    }

    @Test
    fun combinationDoesntBlockSecondMeldCombination() {
        // Family + additional pair
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.TEN),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        assertContainsMeldCombinations(
            listOf(
                AssertedCombination(MeldCombinationType.FAMILY, Suit.BELLS),
                AssertedCombination(MeldCombinationType.PAIR, Suit.BELLS)
            ),
            combinations
        )
    }

    private data class AssertedCombination(val type: MeldCombinationType, val suit: Suit?)

    private fun assertContainsMeldCombinations(
        expected: Collection<AssertedCombination>,
        actual: Collection<MeldCombination>?
    ) {
        val assertedActual = actual?.map { AssertedCombination(it.type, it.suit) }
        assertContainsInAnyOrder(expected, assertedActual)
    }
}

