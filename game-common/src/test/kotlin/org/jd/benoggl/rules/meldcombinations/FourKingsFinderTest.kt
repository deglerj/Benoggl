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
internal class FourKingsFinderTest {

    val sut = FourKingsFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertThat(combinations, Is(empty()))
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.LEAVES, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertThat(combinations, hasSize(1))
        assertThat(combinations.first().type, Is((MeldCombinationType.FOUR_KINGS)))
        assertThat(combinations.first().points, Is((80)))
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertThat(combinations, hasSize(2))
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING)
            ),
            Suit.ACORNS
        )

        assertThat(combinations, Is(empty()))
    }

}