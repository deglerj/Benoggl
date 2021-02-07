package org.jd.benoggl.rules.meldcombinations

import io.quarkus.test.junit.QuarkusTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasSize
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Test
import org.hamcrest.Matchers.`is` as Is

@QuarkusTest
internal class FourUntersFinderTest {

    val sut = FourUntersFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertThat(combinations, Is(empty()))
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertThat(combinations, hasSize(1))
        assertThat(combinations.first().type, Is((MeldCombinationType.FOUR_UNTERS)))
        assertThat(combinations.first().points, Is((40)))
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertThat(combinations, hasSize(2))
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        assertThat(combinations, Is(empty()))
    }

}