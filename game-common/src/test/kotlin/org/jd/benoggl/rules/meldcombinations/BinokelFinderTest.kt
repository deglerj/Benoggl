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
internal class BinokelFinderTest {

    val sut = BinokelFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        assertThat(combinations, Is(empty()))
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

        assertThat(combinations, hasSize(1))
        assertThat(combinations.first().type, Is((MeldCombinationType.BINOKEL)))
        assertThat(combinations.first().points, Is((40)))
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

        assertThat(combinations, Is(empty()))
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

        assertThat(combinations, Is(empty()))
    }

}