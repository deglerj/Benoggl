package org.jd.benoggl.rules.meldcombinations

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.MeldCombination
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Test

@QuarkusTest
internal class MeldCombinationsInCardsFinderTest {

    val sut = MeldCombinationsInCardsFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun noCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

    @Test
    fun singleCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        combinations
            .map(MeldCombination::points)
            .shouldContainExactlyInAnyOrder(20)
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

        combinations
            .map(MeldCombination::points)
            .shouldContainExactlyInAnyOrder(40, 20)
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

        combinations
            .map(MeldCombination::points)
            .shouldContainExactlyInAnyOrder(20, 20)
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

        combinations
            .map(MeldCombination::points)
            .shouldContainExactlyInAnyOrder(100)
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

        combinations
            .map(MeldCombination::points)
            .shouldContainExactlyInAnyOrder(100, 40)
    }

}

