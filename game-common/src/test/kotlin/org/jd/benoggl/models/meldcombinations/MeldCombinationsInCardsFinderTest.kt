package org.jd.benoggl.models.meldcombinations

import io.quarkus.test.junit.QuarkusTest
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Test
import org.hamcrest.Matchers.`is` as Is

@QuarkusTest
internal class MeldCombinationsInCardsFinderTest {

    val sut = MeldCombinationsInCardsFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertThat(combinations, Is(empty()))
    }

    @Test
    fun noCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        assertThat(combinations, Is(empty()))
    }

    @Test
    fun singleCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER)
            ), Suit.ACORNS
        )

        assertThat(
            combinations, containsInAnyOrder(
                hasPoints(20)
            )
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

        assertThat(
            combinations, containsInAnyOrder(
                hasPoints(40),
                hasPoints(20)
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

        assertThat(
            combinations, containsInAnyOrder(
                hasPoints(20),
                hasPoints(20)
            )
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

        assertThat(
            combinations, containsInAnyOrder(
                hasPoints(100)
            )
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

        assertThat(
            combinations, containsInAnyOrder(
                hasPoints(100),
                hasPoints(40)
            )
        )
    }

    private fun hasPoints(points: Int): Matcher<MeldCombination> =
        hasProperty("points", Is(points))


}

