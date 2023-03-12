package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.MeldCombination
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertContentEquals
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

        assertContentEquals(combinations, listOf(MeldCombination(MeldCombinationType.PAIR, 20, Suit.BELLS)))
    }

    @Test
    @Ignore // FIXME
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)

            ), Suit.ACORNS
        )

        assertContentEquals(
            combinations, listOf(
                MeldCombination(MeldCombinationType.TRUMP_PAIR, 40, Suit.ACORNS),
                MeldCombination(MeldCombinationType.PAIR, 20, Suit.BELLS)
            )
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

        assertContentEquals(
            combinations, listOf(
                MeldCombination(MeldCombinationType.PAIR, 20, Suit.BELLS),
                MeldCombination(MeldCombinationType.PAIR, 20, Suit.BELLS)
            )
        )
    }

    @Test
    @Ignore // FIXME
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

        assertContentEquals(
            combinations, listOf(
                MeldCombination(MeldCombinationType.FAMILY, 100, Suit.BELLS)
            )
        )
    }

    @Test
    @Ignore // FIXME
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

        assertContentEquals(
            combinations, listOf(
                MeldCombination(MeldCombinationType.FAMILY, 100, Suit.BELLS),
                MeldCombination(MeldCombinationType.TRUMP_PAIR, 40, Suit.ACORNS)
            )
        )
    }

    //FIXME what about a family & additional pair in one suit?

}

